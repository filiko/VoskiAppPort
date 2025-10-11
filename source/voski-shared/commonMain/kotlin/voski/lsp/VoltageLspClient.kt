package voski.lsp

import voski.VoskiEnv
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.*
import kotlinx.serialization.*

/**
 * Voltage-backed LSP client implementation.
 * 
 * This client communicates with the Voski backend service which orchestrates
 * interactions with the Voltage Lightning node acting as the LSP.
 * 
 * The backend handles:
 * - Channel management via Voltage node APIs
 * - HTLC interception for offline payments
 * - Push notification coordination
 * - Backup encryption key management
 */
class VoltageLspClient : LspClient {
    
    private val config = VoskiEnv.config
    
    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    @Serializable
    private data class EnsureInboundRequest(
        val node_pubkey: String,
        val min_sats: Long
    )
    
    @Serializable
    private data class EnsureInboundResponse(
        val status: String,
        val current_inbound_sats: Long? = null,
        val message: String? = null,
        val error: String? = null
    )
    
    @Serializable
    private data class RouteHintResponse(
        val hints: List<RouteHintJson>
    )
    
    @Serializable
    private data class RouteHintJson(
        val pubkey: String,
        val scid_alias: String,
        val fee_base_msat: Long,
        val fee_proportional: Long,
        val htlc_minimum_msat: Long,
        val htlc_maximum_msat: Long
    )
    
    @Serializable
    private data class DeviceRegisterRequest(
        val node_pubkey: String,
        val platform: String,
        val token: String
    )
    
    @Serializable
    private data class GenericResponse(
        val status: String,
        val error: String? = null
    )
    
    override suspend fun ensureInboundLiquidity(minSats: Long): LspResult<Unit> {
        return try {
            val response = httpClient.post("${config.lspBaseUrl}/lsp/ensure-inbound") {
                contentType(ContentType.Application.Json)
                setBody(EnsureInboundRequest(
                    node_pubkey = "temp_node_pubkey", // TODO: Get actual node pubkey
                    min_sats = minSats
                ))
            }
            
            val result = Json.decodeFromString<EnsureInboundResponse>(response.bodyAsText())
            if (result.status == "OK") {
                LspResult.Ok(Unit)
            } else {
                LspResult.Err("lsp_error", result.error ?: "Unknown error")
            }
        } catch (e: Exception) {
            LspResult.Err("network_error", e.message ?: "Network request failed")
        }
    }
    
    override suspend fun getRouteHints(): LspResult<List<RouteHint>> {
        return try {
            val response = httpClient.get("${config.lspBaseUrl}/lsp/route-hints") {
                parameter("node_pubkey", "temp_node_pubkey") // TODO: Get actual node pubkey
            }
            
            val result = Json.decodeFromString<RouteHintResponse>(response.bodyAsText())
            val hints = result.hints.map { hint ->
                RouteHint(
                    pubkey = hint.pubkey,
                    scidAlias = hint.scid_alias,
                    feeBaseMsat = hint.fee_base_msat,
                    feeProportional = hint.fee_proportional,
                    htlcMinimumMsat = hint.htlc_minimum_msat,
                    htlcMaximumMsat = hint.htlc_maximum_msat
                )
            }
            LspResult.Ok(hints)
        } catch (e: Exception) {
            LspResult.Err("network_error", e.message ?: "Network request failed")
        }
    }
    
    override suspend fun openOrSplice(requiredInboundSats: Long): LspResult<Unit> {
        // Use ensureInboundLiquidity for now - backend handles channel opening/splicing automatically
        return ensureInboundLiquidity(requiredInboundSats)
    }
    
    override suspend fun registerDevice(
        nodePubkey: String,
        platform: String,
        pushToken: String
    ): LspResult<Unit> {
        return try {
            val response = httpClient.post("${config.lspBaseUrl}/devices/register") {
                contentType(ContentType.Application.Json)
                setBody(DeviceRegisterRequest(
                    node_pubkey = nodePubkey,
                    platform = platform,
                    token = pushToken
                ))
            }
            
            val result = Json.decodeFromString<GenericResponse>(response.bodyAsText())
            if (result.status == "OK") {
                LspResult.Ok(Unit)
            } else {
                LspResult.Err("register_error", result.error ?: "Device registration failed")
            }
        } catch (e: Exception) {
            LspResult.Err("network_error", e.message ?: "Network request failed")
        }
    }
    
    override suspend fun getLspInfo(): LspResult<LspInfo> {
        // Return static info from config - this is sufficient for most LSP operations
        return LspResult.Ok(
            LspInfo(
                pubkey = config.lspNodePubkey,
                addresses = listOf("${config.lspNodePubkey}@lsp.voski.com:9735"),
                alias = "Voski LSP"
            )
        )
    }
    
    companion object {
        /**
         * Create a default VoltageLspClient instance.
         */
        fun create(): LspClient = VoltageLspClient()
    }
}