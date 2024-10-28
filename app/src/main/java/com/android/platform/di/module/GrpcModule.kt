package com.android.platform.di.module






import com.android.platform.UserGrpcServiceGrpc
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
        return ManagedChannelBuilder.forAddress("demo.lingomars.ir",443)
            .useTransportSecurity()
            .build()
    }

    @Provides
    @Singleton
    fun provideUserGrpcService(channel: ManagedChannel): UserGrpcServiceGrpc.UserGrpcServiceStub {
        return UserGrpcServiceGrpc.newStub(channel)
    }
}