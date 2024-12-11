package com.android.platform.di.module






import com.android.platform.HomeGrpc
import com.android.platform.LessonGrpc
import com.android.platform.LevelGrpc
import com.android.platform.PlatformApplication
import com.android.platform.SttServiceGrpc
import com.android.platform.UserGrpcServiceGrpc
import com.android.platform.di.factory.BearerTokenInterceptor
import com.android.platform.di.factory.Preferences
import dagger.Module
import dagger.Provides
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import javax.inject.Inject
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
class GrpcModule {

//    @Qualifier
//    @Retention(AnnotationRetention.BINARY)
//    annotation class ChannelLingo

    private val siteUrl="demo.lingomars.ir"
    private val AiUrl="176.120.16.242"

    @Provides
    @SiteURL
    @Singleton
    fun provideSiteUrl(): String = "https://panel.lingomars.ir/images/"



    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class ChannelAI


    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class SiteURL


    @Provides
    @Singleton
    fun provideBearerTokenInterceptor(preferences: Preferences): BearerTokenInterceptor {
        return BearerTokenInterceptor(preferences)
    }


    @Provides
    @Singleton
    fun provideGrpcChannel(bearerTokenInterceptor: BearerTokenInterceptor): ManagedChannel {
        return ManagedChannelBuilder.forAddress(siteUrl, 443)
            .useTransportSecurity()
            .intercept(bearerTokenInterceptor)
            .build()
    }

    @Provides
    @ChannelAI
    @Singleton
    fun provideGrpcChannelAI(): ManagedChannel {
        return ManagedChannelBuilder.forAddress(AiUrl, 2700)
            .usePlaintext()
            .maxInboundMessageSize(10 * 1024 * 1024)
            .build()
    }

    @Provides
    @ChannelAI
    @Singleton
    fun provideSttService(channel: ManagedChannel): SttServiceGrpc.SttServiceStub {
        return SttServiceGrpc.newStub(channel)
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