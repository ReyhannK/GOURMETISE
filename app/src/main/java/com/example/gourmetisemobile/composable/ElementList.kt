package com.example.gourmetisemobile.composable

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gourmetisemobile.dataclass.Bakery
import com.example.gourmetisemobile.R
import com.example.gourmetisemobile.RatingpPage
import com.example.gourmetisemobile.dao.BakeryDAO
import com.example.gourmetisemobile.dao.ContestParamsDAO

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ElementList(
    bakery: Bakery,
    context: Context,
    bakeryDAO: BakeryDAO,
    contestParamsDAO: ContestParamsDAO
){
    val sumNotes = bakeryDAO.getSumNotes(bakery)
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF0F0F0)
        )
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Row (
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ){
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.padding(10.dp),
                )
                Text(
                    text = bakery.name,
                    modifier = Modifier.padding(20.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            IconWithText(text = bakery.getFullAdress(), iconName = stringResource(R.string.address_icon), contentDescription = "", value = "")
            IconWithText(text = bakery.getPhoneNumber(), iconName = stringResource(R.string.phone_icon), contentDescription = "", value = "")
            Row(
                modifier = Modifier.fillMaxWidth().padding(end = 15.dp, bottom = 15.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ){
                if(sumNotes != 0){
                    Text(
                        text = sumNotes.toString() + "/ 15",
                        modifier = Modifier
                            .padding(10.dp),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Justify,
                        style = TextStyle(
                            fontSize = 16.sp, lineHeight = 24.sp
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.Star ,
                        contentDescription = stringResource(R.string.sum_notes),
                        tint = Color(0xFFFFD700),
                    )
                }
                else{
                    Button(
                        modifier = Modifier
                            .height(44.dp)
                            .padding(5.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD9D9D9),
                            contentColor = Color.Black
                        ),
                        onClick = {
                            val intent = Intent(context, RatingpPage::class.java).apply {
                                putExtra("intent_bakery", bakery)
                            }
                            context.startActivity(intent)
                        },
                        enabled = !contestParamsDAO.isOutsidePeriod()
                    ) {
                        Text(stringResource(R.string.discover_btn))
                    }
                }
            }
        }
    }
}