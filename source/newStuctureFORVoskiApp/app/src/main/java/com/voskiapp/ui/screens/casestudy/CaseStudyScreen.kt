package com.voskiapp.ui.screens.casestudy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voskiapp.ui.components.ReusableCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaseStudyScreen(
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Case Studies") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Success Stories",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Text(
                    text = "Learn how others are using Voski App",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            items(caseStudies) { caseStudy ->
                CaseStudyCard(caseStudy)
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CaseStudyCard(caseStudy: CaseStudy) {
    ReusableCard(
        title = caseStudy.title,
        subtitle = caseStudy.company,
        onClick = { /* Navigate to full case study */ }
    ) {
        Text(
            text = caseStudy.summary,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            modifier = Modifier.padding(top = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Results: ${caseStudy.result}",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

data class CaseStudy(
    val title: String,
    val company: String,
    val summary: String,
    val result: String
)

val caseStudies = listOf(
    CaseStudy(
        title = "Digital Transformation Success",
        company = "Tech Corp Inc.",
        summary = "How Tech Corp streamlined their payment processes using Voski App, reducing transaction time by 60% and improving customer satisfaction.",
        result = "60% faster transactions"
    ),
    CaseStudy(
        title = "Small Business Growth",
        company = "Local Bakery",
        summary = "A local bakery increased sales by accepting digital payments through Voski App, attracting more customers and improving cash flow.",
        result = "40% increase in sales"
    ),
    CaseStudy(
        title = "Enterprise Integration",
        company = "Global Retail Chain",
        summary = "Global retailer integrated Voski App across 500+ stores, creating a unified payment experience for millions of customers.",
        result = "500+ stores connected"
    ),
    CaseStudy(
        title = "Freelancer Success Story",
        company = "Independent Designer",
        summary = "How a freelance designer simplified invoice payments and improved client relationships using Voski App's instant payment features.",
        result = "90% faster payments"
    ),
    CaseStudy(
        title = "E-commerce Excellence",
        company = "Online Fashion Store",
        summary = "An online fashion retailer reduced cart abandonment by 35% after implementing Voski App's seamless checkout experience.",
        result = "35% less abandonment"
    )
)


