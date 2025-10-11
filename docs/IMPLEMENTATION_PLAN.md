# Voski Implementation Plan - Definitive Roadmap

> **This is the single source of truth for Voski development. All implementation must follow this plan.**

## Overview

Transform Voski from Phoenix copy into a production-ready Voltage-backed Lightning wallet with:
- Phoenix-style UX (seamless receives, automatic liquidity)
- True self-custody (keys never leave device)  
- Dynamic encrypted backups (restore full channel state, not just force-close)
- LSP integration via Voltage
- Non-custodial backend services

## Phase Status

- ✅ **Phase 1-7**: Core infrastructure implemented (baseline, config, LSP interface, RGS, backups, vouchers, notifications)
- 🔄 **Phase 8**: Backend API implementation (NEXT)
- ⏳ **Phase 9**: Phoenix integration hooks  
- ⏳ **Phase 10**: Production deployment

---

## Phase 8: Backend API Implementation (CURRENT PRIORITY)

### 8A: MVP Backend (Week 1) - Core Voucher System

**Voucher Endpoints (LNURL-withdraw):**
```http
POST /admin/vouchers
Request: {amount_sats: number, description?: string}
Response: {token: string, lnurl: string, expires_at: timestamp}

GET /.well-known/lnurlw/:token
Response: {
  tag: "withdrawRequest",
  callback: "https://api.voski.com/withdraw", 
  k1: string,
  minWithdrawable: number, // msat
  maxWithdrawable: number, // msat
  defaultDescription: string
}

GET /withdraw?k1=:k1&pr=:invoice
Response: {status: "OK"} | {status: "ERROR", reason: string}
```

**Basic LSP Coordination:**
```http
POST /lsp/ensure-inbound
Request: {node_pubkey: string, min_sats: number}
Response: {status: "OK", current_inbound_sats: number} | {error: string}

GET /lsp/route-hints?node_pubkey=:pubkey  
Response: {
  hints: [{
    pubkey: string,
    scid_alias: string, 
    fee_base_msat: number,
    fee_proportional: number,
    htlc_minimum_msat: number,
    htlc_maximum_msat: number
  }]
}

POST /devices/register
Request: {node_pubkey: string, platform: "android"|"ios", token: string}
Response: {status: "OK"} | {error: string}
```

**Implementation Tasks:**
- [ ] Set up Voltage Lightning node (testnet)
- [ ] Create voucher management database schema
- [ ] Implement LNURL-withdraw flow end-to-end
- [ ] Create LSP coordination endpoints
- [ ] Add device registration for push notifications
- [ ] Test voucher redemption with public wallets

### 8B: Dynamic Backup System (Week 2) - Core Differentiator

**Backup Mirroring Endpoints:**
```http
POST /backups/mirror
Request: {
  node_pubkey: string,
  encrypted_blob: base64_string,
  timestamp: number,
  version: number,
  trigger: string // "payment_settled", "channel_opened", etc.
}
Response: {status: "OK", backup_id: string} | {error: string}

GET /backups/latest?node_pubkey=:pubkey
Response: {
  backup_id: string,
  encrypted_blob: base64_string,
  timestamp: number,
  version: number,
  size_bytes: number
} | {error: "not_found"}

GET /backups/list?node_pubkey=:pubkey&limit=10&offset=0
Response: {
  backups: [{
    backup_id: string,
    timestamp: number,
    version: number,
    size_bytes: number,
    trigger: string
  }],
  total_count: number
}

DELETE /backups/old?node_pubkey=:pubkey&keep_latest=5
Response: {deleted_count: number}
```

**Implementation Tasks:**
- [ ] Design encrypted backup storage schema
- [ ] Implement backup mirroring with integrity checks
- [ ] Add backup lifecycle management (cleanup old backups)
- [ ] Create backup health monitoring
- [ ] Test full restore flow from backend mirror

### 8C: Health & Operations (Week 3) - Production Readiness

**Health Monitoring:**
```http
GET /health/routing
Response: {
  success_rate_24h: number, // 0.0-1.0
  avg_response_time_ms: number,
  total_payments_24h: number,
  failed_payments_24h: number
}

GET /health/backups?node_pubkey=:pubkey
Response: {
  last_backup_timestamp: number,
  backup_success_rate_7d: number,
  total_backups: number,
  avg_backup_size_mb: number,
  status: "healthy" | "warning" | "error"
}

POST /telemetry/anonymous
Request: {
  event_type: string,
  platform: string,
  app_version: string,
  data: object // no PII, aggregated metrics only
}
Response: {status: "OK"}
```

**Advanced LSP Coordination:**
```http
POST /lsp/channel-opened
Request: {
  node_pubkey: string,
  channel_id: string,
  capacity_sats: number,
  our_amount_sats: number
}
Response: {status: "OK"}

GET /lsp/status?node_pubkey=:pubkey
Response: {
  channels: [{
    channel_id: string,
    capacity_sats: number,
    local_balance_sats: number,
    remote_balance_sats: number,
    status: "active" | "inactive" | "pending"
  }],
  total_inbound_capacity_sats: number,
  total_outbound_capacity_sats: number
}
```

**Implementation Tasks:**
- [ ] Add health monitoring and metrics collection
- [ ] Implement privacy-preserving telemetry
- [ ] Create LSP status tracking
- [ ] Add operational dashboards
- [ ] Set up alerting for backup failures

---

## Phase 9: Phoenix Integration Hooks

### Route Hints Integration
**Files to modify:**
- Find Phoenix invoice generation code
- Integrate `LspClient.getRouteHints()` into BOLT11 invoice creation
- Ensure route hints are included in all receive scenarios

**Implementation tasks:**
- [ ] Locate Phoenix invoice generation logic
- [ ] Add LSP client dependency injection
- [ ] Integrate route hints into invoice builder
- [ ] Test private channel routing with hints

### Backup Integration Hooks  
**Files to modify:**
- Phoenix payment settlement listeners
- Channel state change handlers
- App lifecycle events

**Implementation tasks:**
- [ ] Hook `BackupService.snapshot()` into payment events
- [ ] Trigger backups on channel state changes
- [ ] Add backup health indicators to UI
- [ ] Implement restore flow in wallet initialization

### Graph Sync Integration
**Files to modify:**  
- App startup sequence (Android Application.onCreate, iOS AppDelegate)
- Phoenix NetworkGraph integration
- Connection management

**Implementation tasks:**
- [ ] Call `GraphSync.refreshIfStale()` on app start
- [ ] Integrate RGS updates with Phoenix routing
- [ ] Add graph sync status to connection UI

---

## Phase 10: Production Deployment & CI/CD

### Backend Deployment
- [ ] Set up production Voltage Lightning node
- [ ] Deploy backend with proper secrets management
- [ ] Configure SSL/TLS and security headers
- [ ] Set up monitoring and logging
- [ ] Create backup and disaster recovery procedures

### Mobile CI/CD
- [ ] GitHub Actions for Android builds
- [ ] GitHub Actions for iOS builds (requires macOS runner)
- [ ] Automated testing pipeline
- [ ] App store deployment automation
- [ ] Beta distribution setup

### Testing & Quality
- [ ] Integration tests for all API endpoints
- [ ] End-to-end voucher redemption tests
- [ ] Backup/restore integration tests
- [ ] Push notification testing
- [ ] Performance and load testing

---

## Implementation Files Reference

### Core Voski Files (Already Implemented ✅)
```
voski-shared/src/
├── commonMain/kotlin/voski/
│   ├── VoskiConfig.kt ✅
│   ├── lsp/
│   │   ├── LspClient.kt ✅  
│   │   └── VoltageLspClient.kt ✅
│   ├── graph/GraphSync.kt ✅
│   ├── backup/BackupService.kt ✅
│   ├── voucher/VoucherRedeemer.kt ✅
│   └── notify/NotificationBridge.kt ✅
├── androidMain/kotlin/voski/VoskiEnv.android.kt ✅
└── iosMain/kotlin/voski/VoskiEnv.ios.kt ✅

voski-android/src/main/kotlin/com/voski/wallet/android/
└── notify/VoskiFcmService.kt ✅
```

### Next Implementation Priority
1. **Backend API endpoints** (Phase 8A-8C)
2. **Phoenix integration hooks** (Phase 9)
3. **HTTP client implementation** in VoltageLspClient.kt
4. **Production deployment** (Phase 10)

---

## Success Metrics & Acceptance Criteria

### Technical Acceptance
- [ ] All API endpoints respond correctly
- [ ] Voucher redemption works end-to-end
- [ ] Backups encrypt/decrypt properly
- [ ] Push notifications wake app successfully  
- [ ] Route hints appear in invoices
- [ ] Full device restore works from backups

### User Experience Acceptance
- [ ] First-time users can receive payments immediately
- [ ] Voucher redemption is one-tap simple
- [ ] Device migration preserves all channel state
- [ ] Payments complete even when app is backgrounded
- [ ] No user-facing errors or crashes

### Production Readiness  
- [ ] 99%+ backup success rate
- [ ] <5 second payment completion
- [ ] Zero funds loss incidents
- [ ] Proper error handling and recovery
- [ ] Comprehensive monitoring and alerting

---

**This plan serves as the definitive roadmap. All development decisions should reference this document. Updates require team consensus and documentation.**