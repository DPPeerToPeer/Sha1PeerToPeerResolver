package com.example.network.internal.data.nodes.myPort

import com.example.network.models.Port

internal interface IMyPortRepository {

    fun getMyPortOrThrow(): Port

    fun setMyPort(port: Port)
}
