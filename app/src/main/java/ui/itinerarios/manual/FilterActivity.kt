package ui.itinerarios.manual

import android.content.Context
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
import com.example.travelillay.data.network.RetrofitClient
import models.itineraries.Actividad
import com.example.travelillay.ui.ActividadAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import com.example.travelillay.data.network.ApiService
import ui.base.BaseActivity

class FilterActivity : BaseActivity() {

    private lateinit var actividadAdapter: ActividadAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var tipoSpinner: Spinner
    private lateinit var progressBar: ProgressBar
    private var actividadesList: List<Actividad> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupMenu()

        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        tipoSpinner = findViewById(R.id.tipoSpinner)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        actividadAdapter = ActividadAdapter(emptyList())
        recyclerView.adapter = actividadAdapter

        configurarSpinner()
        cargarLugaresCercanos()

        searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                ocultarTeclado(searchEditText)
                aplicarFiltros(searchEditText.text.toString(), tipoSpinner.selectedItem.toString())
                true
            } else {
                false
            }
        }
    }

    private fun configurarSpinner() {
        val tipos = resources.getStringArray(R.array.tipo_actividades)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tipos)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tipoSpinner.adapter = adapter

        tipoSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                aplicarFiltros(searchEditText.text.toString(), tipos[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun cargarLugaresCercanos() {
        progressBar.visibility = View.VISIBLE // Muestra la barra de progreso

        val apiService = RetrofitClient.apiService // Usa el apiService ya definido
        val call = apiService.getNearbyPlaces()

        call.enqueue(object : Callback<List<Actividad>> {
            override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                progressBar.visibility = View.GONE

                if (response.isSuccessful) {
                    val actividades = response.body() ?: emptyList()
                    Log.d("FilterActivity", "Actividades recibidas: $actividades") // Log de verificación
                    actividadesList = actividades.map { actividad ->
                        Actividad(
                            name = actividad.name, // Cambié 'nombre' a 'name'
                            rating = actividad.rating,
                            type = actividad.type ?: "Sin Tipo",
                            lat = actividad.lat,
                            lng = actividad.lng
                        )
                    }
                    actividadAdapter.actualizarActividades(actividadesList)
                } else {
                    Log.e("API Error", "Error al cargar actividades: ${response.errorBody()?.string()}")
                    Toast.makeText(this@FilterActivity, "Error al cargar actividades", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("API Error", "Error de conexión: ${t.message}")
                Toast.makeText(this@FilterActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }



    private fun aplicarFiltros(nombre: String, tipo: String) {
        val listaFiltrada = actividadesList.filter {
            it.name.contains(nombre, ignoreCase = true) &&
                    (tipo == "Todos" || it.type.equals(tipo, ignoreCase = true))
        }
        actividadAdapter.actualizarActividades(listaFiltrada)
    }

    private fun ocultarTeclado(view: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}
