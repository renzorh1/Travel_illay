package ui.itinerarios.manual

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
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

class FilterActivity : BaseActivity() { // Heredamos de BaseActivity para reutilizar el menú y la navegación

    private lateinit var actividadAdapter: ActividadAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var tipoSpinner: Spinner
    private var actividadesList: List<Actividad> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Usar setupMenu() de BaseActivity para configurar el menú común
        setupMenu()

        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        tipoSpinner = findViewById(R.id.tipoSpinner)

        recyclerView.layoutManager = LinearLayoutManager(this)
        actividadAdapter = ActividadAdapter(emptyList())
        recyclerView.adapter = actividadAdapter

        configurarSpinner()
        cargarActividades()

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

    private fun cargarActividades() {
        val apiService = RetrofitClient.create(ApiService::class.java)
        val call = apiService.obtenerActividades()

        call.enqueue(object : Callback<List<Actividad>> {
            override fun onResponse(call: Call<List<Actividad>>, response: Response<List<Actividad>>) {
                if (response.isSuccessful) {
                    actividadesList = response.body() ?: emptyList()
                    actividadAdapter.actualizarActividades(actividadesList)
                } else {
                    Log.e("API Error", "Error al cargar actividades: ${response.errorBody()?.string()}")
                    Toast.makeText(this@FilterActivity, "Error al cargar actividades", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<Actividad>>, t: Throwable) {
                Log.e("API Error", "Error de conexión: ${t.message}")
                Toast.makeText(this@FilterActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun aplicarFiltros(nombre: String, tipo: String) {
        val listaFiltrada = actividadesList.filter {
            it.Nombre.contains(nombre, ignoreCase = true) && (tipo == "Todos" || it.Tipo == tipo)
        }
        actividadAdapter.actualizarActividades(listaFiltrada)
    }

    private fun ocultarTeclado(view: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}
