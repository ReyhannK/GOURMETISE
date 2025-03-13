package com.example.gourmetisemobile

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gourmetisemobile.composable.ElementList
import com.example.gourmetisemobile.dao.BakeryDAO
import com.example.gourmetisemobile.dao.ContestParamsDAO
import com.example.gourmetisemobile.dataclass.Bakery
import com.example.gourmetisemobile.dataclass.ContestParams
import com.example.gourmetisemobile.ui.theme.GourmetiseMobileTheme
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GourmetiseMobileTheme {
                val context = LocalContext.current
                val bakeryDao = BakeryDAO(context = context);
                val contestParamsDao = ContestParamsDAO(context = context);
                var messageError by remember { mutableStateOf("") }
                var bakeries by remember { mutableStateOf(mutableListOf<Bakery>()) }
                var isImported by remember { mutableStateOf(false) }
                var searchQuery by remember { mutableStateOf("") }
                bakeries = bakeryDao.getBakeries()
                isImported = bakeries.isNotEmpty()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomAppBar(
                            containerColor = Color(0xFFD9D9D9),
                            modifier = Modifier.height(80.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color(0xFFD9D9D9)),
                                contentAlignment = Alignment.Center
                            )
                            {
                                if(!isImported){
                                    Button(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth(0.6f)
                                            .height(40.dp),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFB8B8B8),
                                            contentColor = Color.Black
                                        ),
                                        onClick = {
                                            val clientHTTP = OkHttpClient()
                                            val request = Request.Builder()
                                                .url("http://10.0.2.2:8000/api/mobile/bakeries")
                                                .build()

                                            clientHTTP.newCall(request).enqueue(object : okhttp3.Callback {
                                                override fun onFailure(call: okhttp3.Call, e: IOException) {
                                                    runOnUiThread { Toast.makeText(context, "ECHEC IMPORT ! "+
                                                            e.toString(), Toast.LENGTH_SHORT).show()
                                                    }
                                                    Log.i("Erreur", e.toString())
                                                }
                                                override fun onResponse(call: okhttp3.Call, response: Response) {
                                                    if (response.isSuccessful) {
                                                        val flux = response.body!!.string()
                                                        Log.i("CodeHTTP", response.code.toString())
                                                        Log.i("REPONSE", flux)
                                                        val jsonResponse  = JSONObject(flux)
                                                        val bakeriesArray  = jsonResponse.getJSONArray("bakeries")
                                                        for (i in 0 until bakeriesArray.length()) {
                                                            val jsonObject: JSONObject = bakeriesArray.getJSONObject(i)
                                                            val b = Bakery()
                                                            b.siret = jsonObject.getString("siret")
                                                            b.name = jsonObject.getString("name")
                                                            b.street = jsonObject.getString("street")
                                                            b.postalCode = jsonObject.getString("postal_code");
                                                            b.city = jsonObject.getString("city");
                                                            b.telephoneNumber = jsonObject.getString("telephone_number");
                                                            b.bakeryDescription = jsonObject.getString("bakery_description");
                                                            b.productsDecription = jsonObject.getString("products_decription");
                                                            bakeryDao.addBakery(b);
                                                        }
                                                        val contestParamsObject  = jsonResponse.getJSONObject("contestParams")
                                                        val c = ContestParams()
                                                        c.startRegistration = contestParamsObject.getString("startRegistration");
                                                        c.endRegistration = contestParamsObject.getString("endRegistration");
                                                        c.startEvaluation = contestParamsObject.getString("startEvaluation");
                                                        c.endEvaluation = contestParamsObject.getString("endEvaluation");
                                                        contestParamsDao.addContestParams(c);

                                                        runOnUiThread { Toast.makeText(context, "IMPORT REUSSI !",
                                                            Toast.LENGTH_SHORT).show()
                                                        }
                                                        bakeries = bakeryDao.getBakeries()
                                                        isImported = true
                                                    } else {
                                                        messageError = JSONObject(response.body!!.string()).getString("message")
                                                    }
                                                }
                                            })
                                        }
                                    ) {
                                        Text(stringResource(R.string.import_btn))
                                    }
                                }

                                if(isImported){
                                    Button(
                                        modifier = Modifier
                                            .padding(5.dp)
                                            .fillMaxWidth(0.6f)
                                            .height(40.dp),
                                        shape = RoundedCornerShape(20.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFB8B8B8),
                                            contentColor = Color.Black
                                        ),
                                        onClick = {}
                                    ) {
                                        Text(stringResource(R.string.export_btn))
                                    }
                                }

                            }
                        }
                    },
                ) {
                    AccueilUI(
                        modifier = Modifier,
                        context,
                        bakeries,
                        messageError,
                        onValidate = { value -> messageError = value},
                        bakeryDao,
                        contestParamsDao
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccueilUI(
    modifier: Modifier = Modifier,
    context: Context,
    bakeries: List<Bakery>,
    messageError: String,
    onValidate: (String) -> Unit,
    bakeryDAO: BakeryDAO,
    contestParamsDAO: ContestParamsDAO,
) {
    val (search, setSearch) = remember { mutableStateOf(TextFieldValue("")) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.home_title),
                fontSize = 24.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(2f)
            )

            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .weight(1f)
            )
        }

        if(bakeries.isEmpty()){
            Card (
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F0F0)
                )
            ) {
                Text(
                    text= stringResource(R.string.welcome),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Text(
                    text= stringResource(R.string.voting_rules),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            if(messageError.isNotEmpty()){
                AlertDialog(
                    onDismissRequest = { onValidate("") },
                    title = { Text(text = stringResource(R.string.alert_dialog_error)) },
                    text = {
                        Text(text = messageError)
                    },
                    confirmButton = {
                        Button(onClick = {
                            onValidate("")
                        }) {
                            Text("OK")
                        }
                    }
                )
            }
        }

        if(bakeries.isNotEmpty()){
            OutlinedTextField(
                value = search,
                onValueChange = { newValue -> setSearch(newValue) },
                singleLine = true,
                placeholder = {
                    Text(
                        stringResource(R.string.search_bakery),
                        style = TextStyle(fontSize = 15.sp)
                    )},
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color(0xFFF0F0F0),
                    focusedBorderColor = Color(0xFFF0F0F0),
                    unfocusedBorderColor = Color(0xFFF0F0F0),
                    cursorColor = Color.Gray
                ),
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth()
                    .height(50.dp),
                trailingIcon = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.magnifying_glass_logo),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp)
            ) {
                items(bakeries) { bakery ->
                    ElementList(bakery = bakery, context, bakeryDAO = bakeryDAO, contestParamsDAO)
                }
            }
        }
    }
}