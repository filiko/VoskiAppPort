# Local Testing Guide - Voski Development

> **How to test the entire Voski system on your computer without external dependencies**

## Overview

This guide shows you how to set up a complete local development environment where you can test:
- ✅ Voski mobile app (Android emulator)
- ✅ Local backend API server  
- ✅ Local Lightning node (LND on regtest/testnet)
- ✅ End-to-end voucher redemption
- ✅ Push notifications (FCM test tokens)
- ✅ Backup encryption/decryption

---

## Setup 1: Local Lightning Node (LND)

### Install LND locally
```bash
# Windows (using chocolatey)
choco install lnd

# Or download from: https://github.com/lightningnetwork/lnd/releases
```

### Create regtest configuration
Create `lnd-regtest.conf`:
```ini
[Application Options]
debuglevel=debug
maxpendingchannels=10
listen=localhost:9735
rpclisten=localhost:10009
restlisten=localhost:8080

[Bitcoin]
bitcoin.active=1
bitcoin.regtest=1
bitcoin.node=bitcoind

[Bitcoind]
bitcoind.rpchost=localhost:18443
bitcoind.rpcuser=regtest
bitcoind.rpcpass=regtest
bitcoind.zmqpubrawblock=tcp://127.0.0.1:28332
bitcoind.zmqpubrawtx=tcp://127.0.0.1:28333
```

### Start local Bitcoin regtest
```bash
# Start bitcoind in regtest mode
bitcoind -regtest -rpcuser=regtest -rpcpassword=regtest -fallbackfee=0.0002 -zmqpubrawblock=tcp://127.0.0.1:28332 -zmqpubrawtx=tcp://127.0.0.1:28333

# In another terminal, start LND
lnd --configfile=lnd-regtest.conf

# Generate some blocks and fund the LND wallet
bitcoin-cli -regtest -rpcuser=regtest -rpcpassword=regtest generatetoaddress 101 <lnd_address>
```

---

## Setup 2: Local Voski Backend Server

### Create minimal backend server
Create `local-backend/server.js`:
```javascript
const express = require('express');
const app = express();
app.use(express.json());

// In-memory storage for testing
const vouchers = new Map();
const devices = new Map();
const backups = new Map();

// Voucher endpoints
app.post('/admin/vouchers', (req, res) => {
  const token = Math.random().toString(36).substring(7);
  const voucher = {
    token,
    amount_sats: req.body.amount_sats || 1000,
    lnurl: `http://localhost:3000/.well-known/lnurlw/${token}`,
    expires_at: Date.now() + (24 * 60 * 60 * 1000), // 24 hours
    redeemed: false
  };
  vouchers.set(token, voucher);
  res.json(voucher);
});

app.get('/.well-known/lnurlw/:token', (req, res) => {
  const voucher = vouchers.get(req.params.token);
  if (!voucher || voucher.redeemed || voucher.expires_at < Date.now()) {
    return res.status(404).json({error: 'Voucher not found or expired'});
  }
  
  res.json({
    tag: 'withdrawRequest',
    callback: `http://localhost:3000/withdraw`,
    k1: voucher.token,
    minWithdrawable: voucher.amount_sats * 1000, // msat
    maxWithdrawable: voucher.amount_sats * 1000, // msat
    defaultDescription: `Voski voucher - ${voucher.amount_sats} sats`
  });
});

app.get('/withdraw', async (req, res) => {
  const {k1, pr} = req.query;
  const voucher = vouchers.get(k1);
  
  if (!voucher || voucher.redeemed) {
    return res.json({status: 'ERROR', reason: 'Invalid voucher'});
  }
  
  try {
    // TODO: Pay the invoice using LND API
    console.log(`Would pay invoice: ${pr}`);
    voucher.redeemed = true;
    res.json({status: 'OK'});
  } catch (error) {
    res.json({status: 'ERROR', reason: error.message});
  }
});

// LSP endpoints (mocked)
app.post('/lsp/ensure-inbound', (req, res) => {
  console.log('LSP: Ensuring inbound liquidity:', req.body);
  res.json({status: 'OK', current_inbound_sats: 100000});
});

app.get('/lsp/route-hints', (req, res) => {
  res.json({
    hints: [{
      pubkey: '03864ef025fde8fb587d989186ce6a4a186895ee44a926bfc370e2c366597a3f8f',
      scid_alias: '1234567890123456',
      fee_base_msat: 1000,
      fee_proportional: 100,
      htlc_minimum_msat: 1000,
      htlc_maximum_msat: 4294967295000
    }]
  });
});

// Push notification registration
app.post('/devices/register', (req, res) => {
  devices.set(req.body.node_pubkey, {
    platform: req.body.platform,
    token: req.body.token,
    registered_at: new Date()
  });
  res.json({status: 'OK'});
});

// Backup endpoints
app.post('/backups/mirror', (req, res) => {
  const backupId = Math.random().toString(36).substring(7);
  backups.set(req.body.node_pubkey, {
    backup_id: backupId,
    encrypted_blob: req.body.encrypted_blob,
    timestamp: req.body.timestamp,
    version: req.body.version,
    trigger: req.body.trigger
  });
  res.json({status: 'OK', backup_id: backupId});
});

app.get('/backups/latest', (req, res) => {
  const backup = backups.get(req.query.node_pubkey);
  if (!backup) {
    return res.status(404).json({error: 'not_found'});
  }
  res.json(backup);
});

app.listen(3000, () => {
  console.log('Voski backend running on http://localhost:3000');
  console.log('Create voucher: POST http://localhost:3000/admin/vouchers');
});
```

### Start the local backend
```bash
cd local-backend
npm init -y
npm install express
node server.js
```

---

## Setup 3: Configure Voski App for Local Testing

### Update VoskiEnv for local development
Edit `voski-shared/src/androidMain/kotlin/voski/VoskiEnv.android.kt`:

```kotlin
actual object VoskiEnv {
    actual val config: VoskiConfig by lazy {
        VoskiConfig(
            lspBaseUrl = "http://10.0.2.2:3000", // Android emulator host
            lspNodePubkey = "03864ef025fde8fb587d989186ce6a4a186895ee44a926bfc370e2c366597a3f8f",
            rgsUrl = null, // Disable RGS for local testing
            backupMode = VoskiConfig.BackupMode.DevicePlusMirror,
            enableVoucher = true
        )
    }
    
    actual val isDebug: Boolean = BuildConfig.DEBUG
    actual val platform: String = "android"
}
```

### Add HTTP client implementation
Update `voski-shared/src/commonMain/kotlin/voski/lsp/VoltageLspClient.kt`:

```kotlin
// Add to build.gradle.kts dependencies:
// implementation("io.ktor:ktor-client-core:2.3.7")
// implementation("io.ktor:ktor-client-json:2.3.7")
// implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.json.*

class VoltageLspClient : LspClient {
    private val httpClient = HttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    
    override suspend fun ensureInboundLiquidity(minSats: Long): LspResult<Unit> {
        return try {
            val response = httpClient.post("${config.lspBaseUrl}/lsp/ensure-inbound") {
                setBody("""{"node_pubkey":"test","min_sats":$minSats}""")
            }
            if (response.status.value == 200) {
                println("[VoltageLspClient] Ensured inbound liquidity: $minSats sats")
                LspResult.Ok(Unit)
            } else {
                LspResult.Err("http_error", "Status: ${response.status}")
            }
        } catch (e: Exception) {
            println("[VoltageLspClient] Error ensuring liquidity: ${e.message}")
            LspResult.Err("network_error", e.message ?: "Unknown error")
        }
    }
    
    override suspend fun getRouteHints(): LspResult<List<RouteHint>> {
        return try {
            val response = httpClient.get("${config.lspBaseUrl}/lsp/route-hints?node_pubkey=test")
            val jsonResponse = json.parseToJsonElement(response.bodyAsText())
            val hintsArray = jsonResponse.jsonObject["hints"]?.jsonArray ?: return LspResult.Ok(emptyList())
            
            val hints = hintsArray.map { hintElement ->
                val hint = hintElement.jsonObject
                RouteHint(
                    pubkey = hint["pubkey"]?.jsonPrimitive?.content ?: "",
                    scidAlias = hint["scid_alias"]?.jsonPrimitive?.content ?: "",
                    feeBaseMsat = hint["fee_base_msat"]?.jsonPrimitive?.long ?: 1000,
                    feeProportional = hint["fee_proportional"]?.jsonPrimitive?.long ?: 100
                )
            }
            
            println("[VoltageLspClient] Got route hints: ${hints.size}")
            LspResult.Ok(hints)
        } catch (e: Exception) {
            println("[VoltageLspClient] Error getting route hints: ${e.message}")
            LspResult.Ok(emptyList()) // Fallback to empty list
        }
    }
    
    // ... implement other methods similarly
}
```

---

## Testing Workflows

### 1. Test Voucher Creation & Redemption

**Create voucher:**
```bash
curl -X POST http://localhost:3000/admin/vouchers \
  -H "Content-Type: application/json" \
  -d '{"amount_sats": 5000}'
```

**Test LNURL-withdraw:**
```bash
# Get the lnurl from the response above
curl "http://localhost:3000/.well-known/lnurlw/YOUR_TOKEN"
```

**Test in app:**
1. Start Android emulator
2. Build and install Voski app
3. Go to Receive screen
4. Tap "Redeem Voucher"
5. Enter the LNURL or scan QR

### 2. Test LSP Integration

**Check route hints:**
```bash
curl "http://localhost:3000/lsp/route-hints?node_pubkey=test"
```

**Test liquidity request:**
```bash
curl -X POST http://localhost:3000/lsp/ensure-inbound \
  -H "Content-Type: application/json" \
  -d '{"node_pubkey":"test","min_sats":10000}'
```

### 3. Test Backup System

**Trigger backup in app:**
- Make a fake payment or channel change
- Check console logs for backup creation
- Verify encrypted blob is posted to `/backups/mirror`

**Test backup retrieval:**
```bash
curl "http://localhost:3000/backups/latest?node_pubkey=test"
```

### 4. Test Push Notifications

**Register device:**
```bash
curl -X POST http://localhost:3000/devices/register \
  -H "Content-Type: application/json" \
  -d '{"node_pubkey":"test","platform":"android","token":"fake_fcm_token"}'
```

**Test FCM locally:**
- Use Firebase console to send test messages
- Or use FCM HTTP API with your test tokens

---

## Debugging Tips

### View Android emulator logs:
```bash
adb logcat | grep -E "(Voski|FCM|Lightning)"
```

### Test with different network conditions:
- Emulator → Settings → Network → Throttle to 3G
- Test offline/online transitions

### Mock Lightning payments:
```javascript
// In your local backend, replace actual LND calls with:
console.log(`MOCK: Would pay invoice ${invoice} for ${amount} sats`);
setTimeout(() => callback(null, 'payment_successful'), 1000);
```

### Test error scenarios:
- Invalid vouchers
- Network timeouts  
- Backup failures
- Push notification delivery issues

---

## Production Testing Checklist

Before deploying to real backend:
- [ ] All local tests pass
- [ ] Error handling works correctly
- [ ] Backup encryption/decryption verified  
- [ ] Network resilience tested
- [ ] Push notifications wake app
- [ ] No crashes or memory leaks
- [ ] Performance acceptable on low-end devices

This local setup lets you develop and test the entire Voski system on your computer before touching any production infrastructure! 🚀