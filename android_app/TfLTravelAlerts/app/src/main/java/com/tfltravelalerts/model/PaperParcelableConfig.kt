package com.tfltravelalerts.model

import paperparcel.PaperParcel
import paperparcel.ProcessorConfig

@Suppress("unused")
@ProcessorConfig(
        options = PaperParcel.Options(
                allowSerializable = false
                )
)
interface PaperParcelConfig {

}