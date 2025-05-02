package com.rafi607062330092.assesment2.screen

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rafi607062330092.assesment2.R
import com.rafi607062330092.assesment2.database.ResepDb
import com.rafi607062330092.assesment2.ui.theme.Assesment2Theme
import com.rafi607062330092.assesment2.util.ViewModelFactory

const val KEY_ID_RESEP = "idResep"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, id: Long? = null) {
    val context = LocalContext.current
    val db = ResepDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var bahan by remember { mutableStateOf(listOf<String>()) }
    var langkah by remember { mutableStateOf(listOf<String>()) }
    var tanggal by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        if (id == null) { return@LaunchedEffect }
        val data = viewModel.getResep(id) ?: return@LaunchedEffect
        judul = data.judul
        kategori = data.kategori
        bahan = data.bahan
        langkah = data.langkah
        tanggal = data.tanggal
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (id == null) {
                        Text(
                            text = stringResource(
                                id = R.string.tambah_resep
                            )
                        )
                    } else {
                        Text(
                            text = stringResource(
                                id = R.string.edit_resep
                            )
                        )
                    }
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            if (judul == "" || kategori == "" || bahan.isEmpty() || langkah.isEmpty()) {
                                Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                                return@IconButton
                            }
                            if (id == null) {
                                viewModel.insert(judul, kategori, bahan, langkah)
                            } else {
                                viewModel.update(id, judul, kategori, bahan, langkah)
                            }
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = stringResource(R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction {
                            showDialog = true
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        FormResep(
            judul = judul,
            kategori = kategori,
            bahan = bahan,
            langkah = langkah,
            tanggal = tanggal,
            onJudulChange = {
                judul = it
            },
            onKategoriChange = {
                kategori = it
            },
            onBahanChange = {
                bahan = it.split(",")
            },
            onLangkahChange = {
                langkah = it.split(",")
            },
            onTanggalChange = {
                tanggal = it
            },
            modifier = Modifier.padding(padding),

        )

        if (id != null && showDialog) {
            DisplayAlertDialog(
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                showDialog = false
                viewModel.delete(id)
                navController.popBackStack()
            }
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = {
        expanded = true
    }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.hapus)
                    )
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@Composable
fun FormResep(judul: String, kategori: String, bahan: List<String>, langkah: List<String>, tanggal: String,
              onJudulChange: (String) -> Unit, onKategoriChange: (String) -> Unit,
              onBahanChange: (String) -> Unit, onLangkahChange: (String) -> Unit,
              onTanggalChange: (String) -> Unit,
              modifier: Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = judul,
            onValueChange = {
                onJudulChange(it)
            },
            label = { Text(text = stringResource(R.string.judul))  },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = kategori,
            onValueChange = {
                onKategoriChange(it)
            },
            label = { Text(text = stringResource(R.string.kategori)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            items.forEach { item ->
                KelasOption(
                    item,
                    kelas == item,
                    modifier = Modifier.selectable(
                        selected = kelas == item ,
                        onClick = { onKelasChange(item) },
                        role = Role.RadioButton
                    ).padding(16.dp).fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun KelasOption(label: String, isSelected: Boolean, modifier: Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected, onClick = null
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assesment2Theme {
        DetailScreen(rememberNavController())
    }
}