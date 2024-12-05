package com.example.gourmetisemobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gourmetisemobile.ui.theme.GourmetiseMobileTheme
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONArray
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
                var bdd = BakeryDAO(context = context);
                var bakeries by remember { mutableStateOf(mutableListOf<Bakery>()) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomAppBar(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.primary,
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            )
                            {
                                Button(
                                    modifier = Modifier
                                        .padding(5.dp)
                                        .fillMaxWidth(0.6f)
                                        .height(40.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    onClick = {
                                        val clientHTTP = OkHttpClient()
                                        val request = Request.Builder()
                                            .url("http://10.0.2.2:8000/api/bakeries")
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
                                                    val fluxJson = JSONArray(flux)
                                                    for (i in 0 until fluxJson.length()) {
                                                        val jsonObject: JSONObject = fluxJson.getJSONObject(i)
                                                        val b = Bakery()
                                                        b.siret = jsonObject.getString("siret")
                                                        b.name = jsonObject.getString("name")
                                                        b.street = jsonObject.getString("street")
                                                        b.postal_code = jsonObject.getString("postal_code");
                                                        b.city = jsonObject.getString("city");
                                                        b.telephone_number = jsonObject.getString("telephone_number");
                                                        b.bakery_description = jsonObject.getString("bakery_description");
                                                        b.products_decription = jsonObject.getString("products_decription");
                                                        bdd.addBakery(b);
                                                    }
                                                    runOnUiThread { Toast.makeText(context, "IMPORT REUSSI !",
                                                        Toast.LENGTH_SHORT).show()
                                                    }
                                                    bakeries = bdd.getBakeries()
                                                } else {
                                                    runOnUiThread { Toast.makeText(context, "ECHEC IMPORT !\n" +
                                                            response.code.toString()+ " "
                                                            +response.message, Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        })

                                    }
                                ) {
                                    Text(stringResource(R.string.import_btn))
                                }
                            }
                        }
                    }
                ) {
                    AccueilUI(modifier = Modifier, bakeries)
                }
            }
        }
    }
}

@Composable
fun AccueilUI(modifier: Modifier = Modifier, bakeries: List<Bakery>) {
    val context = LocalContext.current
    val bdd = BakeryDAO(context = context);
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically){
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
        }

        if(bakeries.isNotEmpty()){
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 70.dp)
            ) {
                items(bakeries) { bakery ->
                    ElementList(bakery = bakery)
                }
            }
        }
    }
}

@Composable
fun ElementList(bakery: Bakery){
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0)
        )
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Column (modifier = Modifier.weight(0.3f)){
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(10.dp),
                )

            }
            Column (modifier = Modifier.weight(0.7f)){
                Text(
                    text = bakery.name,
                    modifier = Modifier.padding(10.dp),
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = bakery.street + " " + bakery.postal_code + " " + bakery.city,
                    modifier = Modifier.padding(start=10.dp, bottom = 10.dp),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = bakery.telephone_number,
                    modifier = Modifier.padding(start=10.dp, bottom = 10.dp),
                    fontWeight = FontWeight.Normal
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        modifier = Modifier
                            .height(44.dp)
                            .padding(5.dp),
                        shape = RoundedCornerShape(2.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD9D9D9),
                            contentColor = Color.Black
                        ),
                        onClick = { /* Action du bouton */ }
                    ) {
                        Text(stringResource(R.string.discover_btn))
                    }
                }
            }
        }
    }
}