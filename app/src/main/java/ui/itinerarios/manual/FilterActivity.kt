package ui.itinerarios.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelillay.R
import models.itineraries.Actividad
import com.example.travelillay.ui.ActividadAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.travelillay.data.network.RetrofitClient
import ui.base.BaseActivity

class FilterActivity : BaseActivity() {

    private var itinerarioId: Int = -1 // Variable para almacenar el ID del itinerario
    private lateinit var actividadAdapter: ActividadAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var tipoSpinner: Spinner
    private lateinit var progressBar: ProgressBar
    private var actividadesList: List<Actividad> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        debeEliminarItinerario = true
        itinerarioId = intent.getIntExtra("itinerarioId", -1)

        setupMenu()

        // Inicializar vistas
        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.recyclerView)
        tipoSpinner = findViewById(R.id.tipoSpinner)
        progressBar = findViewById(R.id.progressBar)

        // Configurar RecyclerView y Adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        actividadAdapter = ActividadAdapter(emptyList()) { actividad ->
            abrirActividadEspecifica(actividad)
        }

        recyclerView.adapter = actividadAdapter

        configurarSpinner()
        cargarLugaresCercanos("Todos")

        // Configurar búsqueda
        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ocultarTeclado()
                aplicarFiltros(searchEditText.text.toString(), tipoSpinner.selectedItem.toString())
                true
            } else {
                false
            }
        }
    }

    override fun onBackPressed() {
        showConfirmationDialog(
            message = "¿Estás seguro de que quieres salir? Se eliminará el itinerario creado.",
            positiveAction = {
                eliminarUltimoItinerario(itinerarioId)
            },
            negativeAction = { super.onBackPressed() }
        )
    }

    private fun configurarSpinner() {
        val tipos = resources.getStringArray(R.array.tipo_actividades)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoSpinner.adapter = adapter

        tipoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                cargarLugaresCercanos(tipos[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun cargarLugaresCercanos(tipo: String) {
        progressBar.visibility = View.VISIBLE // Muestra la barra de progreso

        val apiService = RetrofitClient.apiService
        val typeQuery = if (tipo == "Todos") "" else tipo
        val call = apiService.getNearbyPlaces(typeQuery)

        call.enqueue(object : Callback<List<Actividad>> {
            override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    response.body()?.let { actividades ->
                        Log.d("FilterActivity", "Actividades recibidas: $actividades")
                        actividadesList = actividades
                        actividadAdapter.actualizarActividades(actividadesList)
                    } ?: run {
                        mostrarError("No se encontraron actividades.")
                    }
                } else {
                    mostrarError("Error al cargar actividades: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                progressBar.visibility = View.GONE
                mostrarError("Error de conexión: ${t.message}")
            }
        })
    }

    private fun aplicarFiltros(nombre: String, tipo: String) {
        val listaFiltrada = actividadesList.filter { actividad ->
            actividad.name.contains(nombre, ignoreCase = true) &&
                    (tipo == "Todos" || actividad.type.equals(tipo, ignoreCase = true))
        }
        actividadAdapter.actualizarActividades(listaFiltrada)
    }

    private fun ocultarTeclado() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(searchEditText.windowToken, 0)
        searchEditText.clearFocus()
    }

    private fun mostrarError(mensaje: String) {
        Log.e("FilterActivity", mensaje)
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    private fun abrirActividadEspecifica(actividad: Actividad) {
        val intent = Intent(this, SpecificActivity::class.java).apply {
            putExtra("name", actividad.name) // Pasar el nombre de la actividad
            putExtra("type", actividad.type) // Pasar el tipo de actividad
            putExtra("rating", actividad.rating ?: 0.0) // Pasar el rating
            putExtra("lat", actividad.lat) // Pasar la latitud
            putExtra("lng", actividad.lng) // Pasar la longitud
        }
        startActivity(intent)
    }

}
