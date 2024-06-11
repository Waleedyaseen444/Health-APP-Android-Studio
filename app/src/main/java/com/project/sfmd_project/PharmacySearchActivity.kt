package com.project.sfmd_project

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response
import org.json.JSONArray

class PharmacySearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pharmacy_search)

        val medicineEditText = findViewById<EditText>(R.id.medicineEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)

        searchButton.setOnClickListener {
            val medicineName = medicineEditText.text.toString().trim()
            if (medicineName.isNotEmpty()) {
                searchMedicine(medicineName)
            } else {
                // Handle case where medicine name is empty
            }
        }
    }

    private fun searchMedicine(medicineName: String) {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url("https://drug-info-and-price-history.p.rapidapi.com/1/druginfo?drug=$medicineName")
            .get()
            .addHeader("X-RapidAPI-Key", "8022ecd08dmsh89a3cea93770e44p169825jsn1ad2b997ad86")
            .addHeader("X-RapidAPI-Host", "drug-info-and-price-history.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                runOnUiThread {
                    // Update UI with formatted product information
                    val responseTextView = findViewById<TextView>(R.id.responseTextView)
                    responseTextView.text = formatProductInformation(responseBody)
                }
            }

        })
    }

    private fun formatProductInformation(responseBody: String?): String {
        val formattedProductInfo = StringBuilder()

        if (!responseBody.isNullOrEmpty()) {
            try {
                val jsonArray = JSONArray(responseBody)
                val jsonObject = jsonArray.getJSONObject(0)

                val brandName = jsonObject.optString("brand_name")
                val genericName = jsonObject.optString("generic_name")
                val labelerName = jsonObject.optString("labeler_name")

                // Extracting active ingredients
                val activeIngredientsArray = jsonObject.optJSONArray("active_ingredients")
                val activeIngredients = StringBuilder()
                activeIngredientsArray?.let {
                    for (i in 0 until it.length()) {
                        val activeIngredientObj = it.optJSONObject(i)
                        val ingredientName = activeIngredientObj.optString("name")
                        val ingredientStrength = activeIngredientObj.optString("strength")
                        activeIngredients.append("- $ingredientName: $ingredientStrength\n")
                    }
                }

                // Extracting packaging information
                val packagingArray = jsonObject.optJSONArray("packaging")
                val packagingInfo = packagingArray?.optJSONObject(0)
                val packageSize = packagingInfo?.optString("package_size")
                val packagingIdentifier = packagingInfo?.optString("package_ndc")
                val description = packagingInfo?.optString("description")

                // Extracting marketing details
                val marketingStartDate = packagingInfo?.optString("marketing_start_date")
                val listingExpirationDate = jsonObject.optString("listing_expiration_date")

                // Extracting manufacturer information
                val manufacturerInfo = jsonObject.optJSONObject("openfda")
                val manufacturerName = manufacturerInfo?.optJSONArray("manufacturer_name")?.optString(0)

                // Append formatted information to StringBuilder
                formattedProductInfo.append("Product Information:\n")
                formattedProductInfo.append("- Brand Name: $brandName\n")
                formattedProductInfo.append("- Generic Name: $genericName\n")
                formattedProductInfo.append("- Labeler Name: $labelerName\n\n")

                formattedProductInfo.append("Active Ingredients:\n")
                formattedProductInfo.append(activeIngredients.toString() + "\n")

                formattedProductInfo.append("Packaging Information:\n")
                formattedProductInfo.append("- Package Size: $packageSize\n")
                formattedProductInfo.append("- Packaging Identifier: $packagingIdentifier\n")
                formattedProductInfo.append("- Description: $description\n\n")

                formattedProductInfo.append("Marketing Details:\n")
                formattedProductInfo.append("- Marketing Start Date: $marketingStartDate\n")
                formattedProductInfo.append("- Listing Expiration Date: $listingExpirationDate\n\n")

                formattedProductInfo.append("Manufacturer Information:\n")
                formattedProductInfo.append("- Manufacturer Name: $manufacturerName\n")

            } catch (e: JSONException) {
                e.printStackTrace()
                formattedProductInfo.append("Error parsing product information")
            }
        } else {
            formattedProductInfo.append("Empty response body")
        }

        return formattedProductInfo.toString()
    }

}
