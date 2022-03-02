/*
 * Copyright (c) 2022 Castle
 */

package io.castle.android.api.model

import com.google.gson.annotations.SerializedName

/**
 * Copyright (c) 2022 Castle
 */
data class UserJwt(
    @SerializedName("jwt")
    var jwt: String
    )