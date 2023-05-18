package ba.etf.rma22.planner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextClock
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

public suspend fun checkToday(dan: Int, mjesec: Int, godina: Int): Boolean = withContext(Dispatchers.Default) {
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH) + 1 // Note: Calendar.MONTH is zero-based
    val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

    dan == dayOfMonth && month == mjesec && godina == year
}

public class DailyFragment(dani: DanDAO, stavke: StavkaDAO) : Fragment() {
    val stavke:StavkaDAO=stavke
    val dani:DanDAO=dani
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.daily_fragment, container, false)
        var tClock: TextClock = view.findViewById(R.id.txtClo2);
        var bundle: Bundle? = arguments
        var danas: Boolean? = false
         val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            if (bundle != null) {
                danas = checkToday(
                    bundle.getString("dan")?.toInt()!!,
                    bundle.getString("mjesec")?.toInt()!!,
                    bundle.getString("godina")?.toInt()!!
                )
                if (danas == true) {
                    tClock.visibility = View.GONE
                    tClock.format24Hour = "k:mm";
                    tClock.format12Hour = null;
                }
            }
        }

        var progres: ProgressBar = view.findViewById(R.id.progress_bar2)
        var txtProgress: TextView = view.findViewById(R.id.textView4)
        txtProgress.text = bundle?.getInt("progres").toString() + "%"
        progres.progress = bundle?.getInt("progres")!!
        val recyclerView = view.findViewById<RecyclerView>(R.id.toDo)
        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        recyclerView.addItemDecoration(itemDecoration)
        var arrayLista = bundle.getParcelableArrayList<Stavka>("stavke")
        var lista = mutableListOf<Stavka>()
        if (arrayLista != null) {
            lista.addAll(arrayLista)
        }
        var adapter = ToDoAdapter(lista, dani, stavke, progres, txtProgress, bundle.getInt("danId"))
        recyclerView.adapter = adapter
        val imageButton = view.findViewById<ImageButton>(R.id.imageButton2)
        var textView: TextView = view.findViewById(R.id.textView3)
        val day = bundle.getString("dan")?.toInt()
        val month = bundle.getString("mjesec")?.toInt()
        val year = bundle.getString("godina")?.toInt()

        val calendar = Calendar.getInstance()
        if (year != null) {
            if (month != null) {
                calendar.set(year, month - 1, day!!)
            }
        }

        val currentDate = calendar.time
      //  var currentDate=calendar.time
        val dateFormat = SimpleDateFormat("EEE, MMM dd")
        val formattedDate = dateFormat.format(currentDate)
        textView.text = formattedDate
        imageButton.setOnClickListener {
            var novaStavka = Stavka(naziv = "", uradjeno = false, danId = bundle.getInt("danId"))
            lista.add(novaStavka)
            CoroutineScope(Job() + Dispatchers.Main).launch {
                withContext(Dispatchers.IO){
                stavke.insert(novaStavka);
               var novalista= mutableListOf<Stavka>()
                stavke.getByDate(bundle.getInt("danId"))?.let {it1 -> novalista.addAll(it1)}
                novalista.forEach { l-> println(l.naziv) }
                   val danId=bundle.getInt("danId");
                    val adapter = ToDoAdapter(novalista, dani, stavke, progres, txtProgress,danId)
                    adapter.updateProgress()
                }}


            recyclerView.removeItemDecoration(itemDecoration)
            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            recyclerView.addItemDecoration(itemDecoration)
            recyclerView.adapter = adapter
        }
        return view
    }

}
