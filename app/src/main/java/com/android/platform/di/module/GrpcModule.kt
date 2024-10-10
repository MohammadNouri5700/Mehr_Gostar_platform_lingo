package com.android.platform.di.module


import com.android.platform.BarkReply
import com.android.platform.GreeterProto
import com.android.platform.ServiceFindmyclsGrpc
import dagger.Module
import dagger.Provides
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import javax.inject.Singleton


@Module
class GrpcModule {
    @Provides
    @Singleton
    fun provideGrpcChannel(): ManagedChannel {
        return ManagedChannelBuilder.forAddress("server_address", 50051)
            .usePlaintext()
            .build()
    }

    @Provides
    @Singleton
    fun provideYourGrpcService(channel: ManagedChannel): ServiceFindmyclsGrpc.ServiceFindmyclsStub {
        return ServiceFindmyclsGrpc.newStub(channel)
    }
}