package com.android.platform.di.module






import com.android.platform.HomeGrpc
import com.android.platform.LessonGrpc
import com.android.platform.LevelGrpc
import com.android.platform.PlatformApplication
import com.android.platform.UserGrpcServiceGrpc
import com.android.platform.di.factory.BearerTokenInterceptor
import com.android.platform.di.factory.Preferences
import dagger.Module
import dagger.Provides
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import javax.inject.Inject
import javax.inject.Singleton


@Module
class GrpcModule {



    @Provides
    @Singleton
    fun provideBearerTokenInterceptor(preferences: Preferences): BearerTokenInterceptor {
        return BearerTokenInterceptor(preferences)
    }


    @Provides
    @Singleton
    fun provideGrpcChannel(bearerTokenInterceptor: BearerTokenInterceptor): ManagedChannel {
        return ManagedChannelBuilder.forAddress("demo.lingomars.ir", 443)
            .useTransportSecurity()
            .intercept(bearerTokenInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideUserGrpcService(channel: ManagedChannel): UserGrpcServiceGrpc.UserGrpcServiceStub {
        return UserGrpcServiceGrpc.newStub(channel)
    }
    @Provides
    @Singleton
    fun provideHomeGrpc(channel: ManagedChannel): HomeGrpc.HomeStub {
        return HomeGrpc.newStub(channel)
    }
    @Provides
    @Singleton
    fun provideLevelGrpc(channel: ManagedChannel): LevelGrpc.LevelStub {
        return LevelGrpc.newStub(channel)
    }
    @Provides
    @Singleton
    fun provideLessonGrpc(channel: ManagedChannel): LessonGrpc.LessonStub {
        return LessonGrpc.newStub(channel)
    }


}