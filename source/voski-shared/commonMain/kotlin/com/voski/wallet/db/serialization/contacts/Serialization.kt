package com.voski.wallet.db.serialization.contacts

import com.voski.wallet.data.ContactInfo

object Serialization {

    fun serialize(contact: ContactInfo): ByteArray {
        return com.voski.wallet.db.serialization.contacts.v1.Serialization.serialize(contact)
    }

    fun deserialize(bin: ByteArray): Result<ContactInfo> {
        return runCatching {
            when (val version = bin.first().toInt()) {
                1 -> com.voski.wallet.db.serialization.contacts.v1.Deserialization.deserialize(bin)
                else -> error("unknown version $version")
            }
        }
    }
}