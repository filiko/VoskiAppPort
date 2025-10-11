# Purpose: Voski — Phoenix-Style Self-Custody Lightning Wallet

## Overview

Voski aims to deliver a self-custodial Lightning wallet that feels as seamless as custodial apps, while keeping private keys and channel state entirely on the user's device. Inspired by Phoenix Wallet, Voski goes further by introducing encrypted dynamic backups so users can fully restore their Lightning channels — not just force-close them — across devices.

## Core Goals

### Self-Custody First
- User's device is the Lightning node (via LDK + BDK)
- Keys and channel state never leave the phone
- No custodial risk or trust assumptions

### Seamless UX (like Phoenix)
- One-tap onboarding
- Instant receive with inbound liquidity provided by an LSP (Voltage)
- Payments "just work," even when the app is offline (hold-invoice + push-to-wake flow)

### Encrypted Dynamic Backups
- After every channel update, the app serializes ChannelManager + ChannelMonitors, encrypts them with a seed-derived key, and uploads to iCloud/Google Drive
- Voski backend can optionally mirror encrypted blobs (ciphertext only) for redundancy
- This enables true channel state restore on a new device (beyond Phoenix's static channel backup)

### Pluggable LSP Integration
- Start with Voltage as our first Liquidity Service Provider
- Keep the integration swappable to avoid lock-in

### Non-Custodial Backend Services (Voski)
- **Notification relay**: wake user devices when an incoming HTLC is held
- **Voucher system**: LNURL-withdraw flow to fund wallets via gift cards
- **Ops & telemetry**: monitor routing health, backup success, and liquidity — without ever holding user keys or plaintext channel state

## What Voski Is (and Isn't)

### Is:
- A mobile Lightning node running on the user's phone
- A supporting backend for vouchers, notifications, and encrypted backup mirroring
- A bridge to liquidity via a hosted Voltage node

### Is Not:
- A custodial service
- A place where user private keys or unencrypted channel data are stored
- A replacement for the device's Lightning node (the device itself is the node)

## Why It Matters

Most wallets today either:
- Offer great UX but are custodial (user doesn't control their funds), or
- Offer self-custody but clunky UX (users must manage liquidity, backups, and uptime)

Voski bridges this gap:
- **Self-custody** with Phoenix-like ease of use
- **Dynamic backups** for seamless device restores
- **LSP support** for instant liquidity without trusting a custodian

The result: a Lightning wallet that "just works" for users, while staying true to Bitcoin's principle of self-sovereignty.

## Core Principles (Non-Negotiables)

1. **Self-custody**: Private keys live only on the device
2. **Silent safety**: After every channel update, the phone encrypts the new state (seed-derived key) and uploads backups automatically
3. **Restore honesty**: Seed unlocks backup decryption; if backups are missing, fall back to a Static Channel Backup (SCB) to force-close on-chain
4. **Mobile reality**: Receiving while the app is asleep uses short payment holds + push-to-wake, not magic
5. **Pluggable LSP**: Start with one partner; keep the integration swappable to avoid lock-in
6. **Monolithic backend (Voski)**: One service for vouchers, notifications, node access, and encrypted backup mirroring (ciphertext only)

## Technical Architecture

### Mobile App (Android + iOS)
- UI layer with QR scan, onboarding, restore wizard, "backup health" checks
- Talks to Wallet Core via FFI (bridge from app to Rust)

### Wallet Core (Rust with LDK + BDK)
- **LDK**: Lightning channels, payments, routing
- **BDK**: On-chain funding, fee bumping, UTXO hygiene
- **Backups**: Serialize manager + per-channel monitors → AEAD-encrypt using key derived from seed (HKDF) → upload to iCloud/Drive (+ optional Voski mirror)
- **Restore**: Seed → derive key → fetch encrypted blobs → decrypt → rebuild ChannelManager + ChannelMonitors

### Voski Backend (Monolith)
- **Vouchers/Gift cards**: LNURL-withdraw flow (scan QR → wallet sends invoice → Voski pays from its node)
- **Notification relay**: Store device push tokens; wake app to finalize held incoming payments
- **Encrypted backup mirror** (optional): Store only ciphertext blobs + minimal headers for integrity/indexing
- **Admin/ops**: Metrics for routing, voucher status, backup success, logs
- **Lightning node**: Hosted (e.g., Voltage) using standard LND/CLN APIs for payouts/liquidity checks

## Key User Flows

### A) Gift Card → Wallet (Funding)
1. User scans LNURL-withdraw QR on card
2. Wallet fetches withdraw metadata, creates an invoice, calls Voski callback
3. Voski pays invoice from its node → funds land in the user's wallet
4. App requests LSP to ensure inbound liquidity for future receives

### B) Backup & Restore
1. Any channel update → phone encrypts new state → uploads to iCloud/Drive (+ Voski mirror)
2. New phone → user enters seed → app derives backup key → fetches + decrypts latest blobs → restores channels
3. If blobs missing → use SCB PDF/QR to force-close and recover on-chain

## Development Phases

### Phase 1 — Voucher Prototype (1–2 weeks)
- Spin up hosted Lightning node (testnet), fund it
- Build Voski voucher endpoints + DB
- Print an LNURL-withdraw QR and redeem with a public wallet (end-to-end proof)
- **Exit**: QR → sats move from Voski node to a wallet reliably

### Phase 2 — Wallet Core & Backups (3–4 weeks)
- LDK send/receive on testnet in Rust lib; expose FFI to app
- Implement encrypt-and-upload after every state change to iCloud/Drive (+ optional Voski mirror); retry queue
- Restore drills on fresh device: normal (from cloud) and worst-case (SCB force-close)
- **Exit**: 10/10 restore scenarios pass; no lost states

### Phase 3 — App UX + LSP Integration (3–5 weeks)
- App onboarding, seed setup, "backup healthy" guardrails, QR scanner
- Integrate initial LSP; push-to-wake receive; clear held-payment UX
- Telemetry (privacy-preserving), crash reporting, fee/UTXO hygiene
- **Exit**: High payment success rate; backup failures near zero; ready for limited mainnet beta

### Phase 4 — Advanced (feature-flagged)
- Splicing (resize channel without close), Offers/BOLT12, LNURL-Auth
- Multi-LSP fallback; optional watchtower; on/off-ramp connectors (KYC-gated)

## Risk Mitigation

- **Backup race**: Atomic local write → enqueue upload → don't "complete" UI until snapshot queued; multi-destination backups
- **App sleeping**: Rely on holds + push notifications; show clear expiry windows
- **Vendor lock-in**: LSP integration is pluggable; add a second partner in Phase 3
- **Tiny vouchers eaten by fees**: Set sensible minimum (e.g., ≥10–20k sats)
- **Compliance**: Vouchers sold for fiat may trigger gift-card / money-transmitter rules; start on testnet and/or use licensed partner; KYC only for fiat rails

## Success Metrics

### V1 Acceptance Criteria
- Redeem voucher QR with any compatible wallet and with our app
- After any Lightning action, a new encrypted backup is queued/uploaded
- Full restore on a new phone using only seed + cloud succeeds; SCB fallback documented
- New users can receive after first run via LSP; held-payment + wake flow works within timeout

### Long-term Goals
- 99%+ backup success rate
- <5 second payment completion time
- Zero lost funds due to backup failures
- Seamless device migration experience

## Glossary

- **LSP (Liquidity partner)**: A big Lightning node that opens/channels liquidity to your users so receiving "just works"
- **SCB (Static Channel Backup)**: A safety file to force-close channels on-chain if dynamic backups are missing
- **LNURL-withdraw**: Standard "scan QR to withdraw" flow (voucher → wallet invoice → server pays)
- **FFI (App ↔ Rust bridge)**: Lets the mobile app call the Rust wallet core
- **AEAD / HKDF**: Encryption & key-derivation used to protect backups using a key derived from the user's seed

---

*Voski represents the next evolution in Lightning wallet design: combining the security of self-custody with the convenience users expect from modern financial apps.*
