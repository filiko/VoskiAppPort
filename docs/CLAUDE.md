# Agent Contract

You are a coding assistant working **plan-first** with strict logging.

## Repository Structure

**VoskiBackend** is a Phoenix-style self-custody Bitcoin wallet project with this structure:
- **Main repo**: `VoskiBackend/` - Active development area
- **Reference repo**: `VoskiBackend/Phoenix/phoenix-master/` - **READ-ONLY** source material from ACINQ's Phoenix wallet

**CRITICAL**: Never modify anything in `VoskiBackend/Phoenix/` - it's reference-only material for copying from.

## Workflow
1) PLAN: Create/refresh `PLAN.md` with:
   - Scope & success criteria
   - File map (files to add/edit/remove from main repo)
   - Test plan & run commands
   - Risks/assumptions
2) IMPLEMENT: Make small, reversible edits to main repo only.
3) VERIFY: Run tests/linters/commands (simulate if tools unavailable). Summarize outcomes.
4) LOG: Append a new entry to `CHANGELOG.md`:
   - Timestamp (UTC), task, rationale
   - Files touched in main repo
   - Patch summaries (unified diff snippet or bullet changes)
   - Follow-ups/next steps
5) CHECKLIST: Confirm acceptance criteria satisfied. If not, revise PLAN and repeat.

## Editing Rules
- Never make large multi-file edits in one step.
- Prefer minimally invasive changes; preserve API contracts.
- Only modify files in the main VoskiBackend directory, never in Phoenix/
- When copying from Phoenix reference, adapt to new project structure
- If uncertain, ask for the missing requirement **inside PLAN.md** under "Open Questions".

## Output Format
- When I ask for work: 
  - Update `PLAN.md` first (full content).
  - Then propose changes with **diff-like blocks** per file:
    ```diff
    --- path/to/file.ext
    +++ path/to/file.ext
    @@ context @@
    - old line
    + new line
    ```
- After edits, append a `CHANGELOG.md` entry.

## Project Architecture (from Phoenix reference)

Multi-module Kotlin Multiplatform application:
- **phoenix-shared**: Business logic in Kotlin, shared between iOS and Android (uses lightning-kmp v1.10.6)
- **phoenix-android**: Android UI with Jetpack Compose
- **phoenix-ios**: iOS UI in Swift

## Build Commands
- **Android**: `./gradlew :phoenix-android:assembleDebug`
- **Tests**: `./gradlew test`
- **Docker Release**: `docker build -t phoenix_build . && docker run...`

## Technical Constraints
- Min Android SDK: 26, Target: 35
- Java toolchain: 17, Kotlin: 2.1.10
- Dependencies managed in `gradle/libs.versions.toml`
- Self-custodial wallet with 12-word recovery phrases