package org.example.sha1PeerToPeer.domain.models

data class Batch(
    val start: String,
    val end: String,
) {

    init {
        require(start <= end) {
            "Batch start should be smaller or equal then batch end"
        }
    }
}
