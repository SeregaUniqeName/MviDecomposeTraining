package com.example.mvidecomposetraining

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.example.mvidecomposetraining.presentation.root.RootComponentImpl
import com.example.mvidecomposetraining.presentation.root.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: RootComponentImpl.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApp).component.inject(this)
        super.onCreate(savedInstanceState)
        val root = rootComponentFactory.create(defaultComponentContext())
        setContent {
            RootContent(component = root)
        }
    }
}
