import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color

@Composable
fun MiarteBaseScreen() {
    Box(
        modifier = Modifier.fillMaxSize() // le Box crée un scope pour align()
    ) {
        // Contenu principal (barre + catégories)
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBarApp()
            Spacer(modifier = Modifier.height(8.dp))
            CategoryList()
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ButtonAdd()
        }
    }
}
@Composable
fun TopBarApp(modifier: Modifier = Modifier) {
    Surface(
        color = Color.Gray,
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "MiArte",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun CategoryList(modifier: Modifier = Modifier) {
    val categories = listOf("Dessin", "Musique", "Peinture", "Écriture", "Jeux Vidéos")

    LazyRow(
        modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
            )
        }
    }
}

@Composable
fun CategoryChip(category: String) {
    Surface(
        color = Color.LightGray,
        shape = androidx.compose.material3.MaterialTheme.shapes.medium,
        modifier = Modifier
    ) {
        Text(
            text = category,
            color = Color.Black,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
fun ButtonAdd() {
    FloatingActionButton(
        onClick = { /* TODO: action du bouton + */ },
        containerColor = Color.Gray,
        modifier = Modifier.padding(bottom = 24.dp)
    ) {
        Text(
            text = "+",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MiArteBaseScreenPreview() {
    MiarteBaseScreen()
}
