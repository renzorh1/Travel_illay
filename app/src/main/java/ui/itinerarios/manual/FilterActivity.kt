package ui.itinerarios.manual

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import android.widget.Button
import com.example.travelillay.data.network.RetrofitClient
import ui.base.BaseActivity
import models.auth.requests.RelacionRequest
import models.auth.responses.RelacionResponse
import ui.principal.PrincipalActivity

class FilterActivity : BaseActivity() {

    private val actividadesSeleccionadas = mutableListOf<Int>()
    override var itinerarioId: Int = -1 // Cambiado a Int
    private lateinit var actividadAdapter: ActividadAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var tipoSpinner: Spinner
    private lateinit var progressBar: ProgressBar
    private var actividadesList: List<Actividad> = emptyList()
    override var usuarioId: Int = -1

    companion object {
        const val REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itinerarioId = intent.getIntExtra("itinerarioId", -1)

        if (itinerarioId != -1) {
            Log.d("FilterActivity", "Itinerario ID recibido: $itinerarioId")
            Toast.makeText(this, "Itinerario ID recibido: $itinerarioId", Toast.LENGTH_SHORT).show()
            // Aquí puedes cargar actividades asociadas a este itinerario ID si es necesario
        } else {
            showToast("ID de itinerario no válido")
            finish()
        }

        setupMenu()

        searchEditText = findViewById(R.id.searchEditText)
        recyclerView = findViewById(R.id.recyclerView)
        tipoSpinner = findViewById(R.id.tipoSpinner)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        actividadAdapter = ActividadAdapter(emptyList()) { actividad -> abrirActividadEspecifica(actividad) }
        recyclerView.adapter = actividadAdapter

        configurarSpinner()
        cargarLugaresCercanos("Todos")

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ocultarTeclado()
                aplicarFiltros(searchEditText.text.toString(), tipoSpinner.selectedItem.toString())
                true
            } else {
                false
            }
        }

        findViewById<Button>(R.id.crearItinerarioButton).setOnClickListener {
            if (actividadesSeleccionadas.isNotEmpty()) {
                guardarItinerarioActividades()
            } else {
                Toast.makeText(this, "No se han seleccionado actividades.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardarItinerarioActividades() {
        val apiService = RetrofitClient.apiService
        val relaciones = actividadesSeleccionadas.map { RelacionRequest(itinerarioId, it) }
        val totalRelaciones = relaciones.size
        var relacionesGuardadas = 0

        for (request in relaciones) {
            apiService.guardarRelacionItinerarioActividad(request).enqueue(object : Callback<RelacionResponse> {
                override fun onResponse(call: Call<RelacionResponse>, response: Response<RelacionResponse>) {
                    if (response.isSuccessful) {
                        Log.d("FilterActivity", "Relación guardada: $request")
                    } else {
                        val errorMsg = response.errorBody()?.string() ?: "Error desconocido"
                        mostrarError("Error al guardar relación: $errorMsg")
                    }
                    relacionesGuardadas++
                    verificarSiTodasGuardadas(totalRelaciones, relacionesGuardadas)
                }

                override fun onFailure(call: Call<RelacionResponse>, t: Throwable) {
                    mostrarError("Error de conexión: ${t.message}")
                    relacionesGuardadas++
                    verificarSiTodasGuardadas(totalRelaciones, relacionesGuardadas)
                }
            })
        }
    }

    private fun verificarSiTodasGuardadas(total: Int, guardadas: Int) {
        if (guardadas == total) {
            Toast.makeText(this, "Actividades guardadas en el itinerario.", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, PrincipalActivity::class.java)
            startActivity(intent)
            finish()
        }
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
        progressBar.visibility = View.VISIBLE
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
                mostrarError("Error de conexión: ${t.message}. ¿Deseas intentar nuevamente?")
            }
        })
    }

    private fun aplicarFiltros(nombre: String, tipo: String) {
        val listaFiltrada = actividadesList.filter { actividad ->
            actividad.nombre.contains(nombre, ignoreCase = true) &&
                    (tipo == "Todos" || actividad.tipo.equals(tipo, ignoreCase = true))
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
            putExtra("name", actividad.nombre)
            putExtra("itinerarioId", itinerarioId) // Pasar itinerarioId a SpecificActivity
        }
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            data?.getIntExtra("actividadId", -1)?.let { actividadId ->
                actividadesSeleccionadas.add(actividadId)
                Toast.makeText(this, "Actividad añadida al itinerario.", Toast.LENGTH_SHORT).show()
                cargarLugaresCercanos(tipoSpinner.selectedItem.toString())
            }
        }
    }
}
