package com.android.platform.di.factory

import com.android.platform.PlatformApplication
import io.grpc.*

class BearerTokenInterceptor() : ClientInterceptor {
    override fun <ReqT, RespT> interceptCall(method: MethodDescriptor<ReqT, RespT>,
                                             callOptions: CallOptions, next: Channel): ClientCall<ReqT, RespT> {
        return object : ForwardingClientCall.SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<RespT>, headers: Metadata) {
                headers.put(Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER), "Bearer ${PlatformApplication.token}")
                super.start(responseListener, headers)
            }
        }
    }
}
