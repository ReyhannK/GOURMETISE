package com.example.gourmetisemobile

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.ui.res.stringResource
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gourmetisemobile.composable.IconWithText
import com.example.gourmetisemobile.composable.RatingBar
import com.example.gourmetisemobile.dao.BakeryDAO
import com.example.gourmetisemobile.dao.NoteDAO
import com.example.gourmetisemobile.dataclass.Bakery
import com.example.gourmetisemobile.dataclass.Note
import com.example.gourmetisemobile.ui.theme.GourmetiseMobileTheme

class RatingpPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GourmetiseMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val bakery = (context as? Activity)?.intent?.extras?.getParcelable<Bakery>("intent_bakery")

                    val bakeryDao = BakeryDAO(context = context);
                    val noteDao = NoteDAO(context = context);

                    if (bakery != null) {
                        RatingUI(
                            modifier = Modifier.padding(innerPadding),
                            context,
                            bakery,
                            bakeryDao,
                            noteDao
                        )
                    }
                    else{
                        Text(
                            text = stringResource(R.string.bakery_not_found),
                            modifier = Modifier.padding(start=10.dp, bottom = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("ShowToast")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingUI(modifier: Modifier = Modifier, context: Context, bakery: Bakery, bakeryDAO: BakeryDAO, noteDAO: NoteDAO) {
    val scrollState = rememberScrollState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()

    Column(modifier = Modifier.verticalScroll(scrollState)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 10.dp)
                    .weight(0.5f),
                onClick = {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                    context.startActivity(intent)
                    (context as? Activity)?.finish()
                }
            ) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Retour")
            }

            Text(
                text = bakery.name,
                modifier = Modifier
                    .padding(start = 10.dp, bottom = 10.dp)
                    .weight(1f),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    fontSize = 18.sp
                ),
            )
            Spacer(modifier = Modifier.weight(0.5f))
        }
        IconWithText(text = stringResource(R.string.address), iconName = stringResource(R.string.address_icon), contentDescription = "", value = bakery.getFullAdress())
        IconWithText(text = stringResource(R.string.phone_number), iconName = stringResource(R.string.phone_icon), contentDescription = "", value = bakery.getPhoneNumber())
        Description(
            title = stringResource(R.string.bakery_description),
            content = bakery.bakeryDescription,
            iconName = stringResource(R.string.house_icon),
            contentDescription = "icon de maison"
        )

        HorizontalDivider(
            modifier = Modifier.padding(start = 10.dp, end = 10.dp),
            thickness = 1.dp,
            color = Color.Gray
        )

        Description(
            title = stringResource(R.string.product_description),
            content = bakery.productsDecription,
            iconName = stringResource(R.string.croissant_icon),
            contentDescription = "icon de croissant"
        )
        Button(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth(0.6f)
                .height(45.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Red,
                contentColor = Color.White
            ),
            onClick = { showBottomSheet = true}
        ) {
            Text(stringResource(R.string.rate_bakery_btn))
        }
    }

    if(showBottomSheet){
        var errorMessage by remember { mutableStateOf<String?>(null) }

        var code by remember { mutableStateOf("") }

        val criteriaMap = remember { mutableStateMapOf<String, Int>(
            "reception" to 1,
            "product" to 1,
            "decoration" to 1
        )}

        val criteriaIds = mapOf(
            "reception" to 1,
            "product" to 2,
            "decoration" to 3
        )

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            modifier = Modifier.fillMaxHeight(1f)
        ){
            Text(
                text = stringResource(R.string.rating_title),
                modifier = Modifier
                    .padding(start = 20.dp),
                fontWeight = FontWeight.Medium,
                style = TextStyle(
                    fontSize = 18.sp
                ),
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    label = { Text(stringResource(R.string.rating_purchase_code)) },
                    singleLine = true,
                    isError = errorMessage != null,
                )
                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                criteriaMap["reception"]?.let {
                    RatingBar(currentRating = it, title = stringResource(R.string.rating_reception_criteria)) { newRating -> criteriaMap["reception"] = newRating
                    }
                }
                criteriaMap["product"]?.let {
                    RatingBar(currentRating = it, title = stringResource(R.string.rating_product_criteria)) { newRating -> criteriaMap["product"] = newRating
                    }
                }
                criteriaMap["decoration"]?.let {
                    RatingBar(currentRating = it, title = stringResource(R.string.rating_decoration_criteria)) { newRating -> criteriaMap["decoration"] = newRating
                    }
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically){
                Button(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    onClick = { showBottomSheet = false}
                ) {
                    Text(stringResource(R.string.cancel))
                }
                Button(
                    modifier = Modifier
                        .padding(10.dp)
                        .height(35.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Green,
                        contentColor = Color.White
                    ),
                    onClick = {
                        if(code.length != 6){
                            errorMessage = "Le code doit comporter exactement 6 caractères"
                        }else{
                            bakeryDAO.updateBakery(bakery = bakery, codeTicket = code)
                            criteriaIds.forEach { (key, id) ->
                                criteriaMap[key]?.let { rating ->
                                    val note = bakery.siret?.let {
                                        Note(bakerySiret = it, criteriaId = id, value = rating)
                                    }
                                    if (note != null) {
                                        noteDAO.addNote(note)
                                    }
                                }
                            }
                            showBottomSheet = false
                            errorMessage = null
                            Toast.makeText(context, "Notes enregistrées ! ", Toast.LENGTH_SHORT).show()

                            context.startActivity(Intent(context, MainActivity::class.java))
                        }
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        }
    }
}

@Composable
fun Description(title: String,content: String, iconName: String, contentDescription: String){
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ){
        Column (modifier = Modifier.fillMaxWidth()){
            IconWithText(text = title, iconName = iconName, contentDescription = contentDescription, value="")
            Text(
                text = content.ifEmpty { stringResource(R.string.no_content) },
                modifier = Modifier
                    .padding(top = 15.dp, bottom = 20.dp, start = 40.dp, end = 40.dp),
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Justify,
                style = TextStyle(
                    fontSize = 16.sp, lineHeight = 24.sp
                )
            )
        }
    }
}


