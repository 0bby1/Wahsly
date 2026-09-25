
package com.example.wahsly.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wahsly.datos.repository.FirebaseUsuarioRepository

class ViewModelFactory(
    private val firebaseUsuarioRepository:
    FirebaseUsuarioRepository = FirebaseUsuarioRepository()
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        return when {

            modelClass.isAssignableFrom(
                RegistroViewModel::class.java
            ) -> {

                RegistroViewModel(
                    firebaseUsuarioRepository
                ) as T

            }

            modelClass.isAssignableFrom(
                CuentaViewModel::class.java
            ) -> {

                CuentaViewModel() as T

            }

            else -> {

                throw IllegalArgumentException(
                    "ViewModel desconocido: ${modelClass.name}"
                )

            }

        }

    }

}
