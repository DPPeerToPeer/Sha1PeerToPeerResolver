package com.example.network.internal.data.nodes.myPort

import com.example.network.models.Port

internal class MyPortRepository : IMyPortRepository {

    private var myPort: Port? = null

    override fun getMyPortOrThrow(): Port =
        myPort!!

    override fun setMyPort(port: Port) {
        myPort = port
    }
}
