package com.example.gourmetisemobile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                    if (bakery != null) {
                        RatingUI(
                            modifier = Modifier.padding(innerPadding),
                            context,
                            bakery
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

@Composable
fun RatingUI(modifier: Modifier = Modifier, context: Context, bakery: Bakery) {
    val scrollState = rememberScrollState()
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
        IconWithText(text = stringResource(R.string.phone_number), iconName = stringResource(R.string.phone_icon), contentDescription = "", value = bakery.telephone_number)
        Description(
            title = stringResource(R.string.bakery_description),
            content = bakery.bakery_description,
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
            content = bakery.products_decription,
            iconName = stringResource(R.string.croissant_icon),
            contentDescription = "icon de croissant"
        )
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

@Composable
fun IconWithText(text: String, contentDescription: String, iconName: String, value: String) {
    val iconResId = when (iconName) {
        "house" -> R.drawable.house
        "croissant" -> R.drawable.croissant
        "address" -> R.drawable.map
        "phone" -> R.drawable.phone
        else -> android.R.drawable.ic_menu_close_clear_cancel
    }
    Card(
        modifier = Modifier
            .padding(start = 16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = contentDescription.ifEmpty { stringResource(R.string.no_content) },
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(35.dp).padding(end = 10.dp),
            )
            Row(modifier = Modifier.weight(1f)) {
                Text(
                    text = text.ifEmpty { stringResource(R.string.no_content) },
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(fontSize = 16.sp),
                )
                Text(
                    text = value,
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(fontSize = 16.sp),
                )
            }
        }
    }
}

