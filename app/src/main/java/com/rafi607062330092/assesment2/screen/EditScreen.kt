package com.rafi607062330092.assesment2.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rafi607062330092.assesment2.R
import com.rafi607062330092.assesment2.database.ResepDb
import com.rafi607062330092.assesment2.ui.theme.ThemeController
import com.rafi607062330092.assesment2.util.ViewModelFactory

const val KEY_ID_RESEP = "idResep"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(navController: NavController, id: Long? = null) {
    val context = LocalContext.current
    val db = ResepDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var bahan by remember { mutableStateOf(listOf<String>()) }
    var langkah by remember { mutableStateOf("") }
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
                            if (judul.isBlank() || kategori.isBlank() || bahan.isEmpty() || langkah.isBlank()) {
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
            onJudulChange = {
                judul = it
            },
            onKategoriChange = {
                kategori = it
            },
            onBahanChange = { it ->
                bahan = if (it.isBlank()) {
                    listOf()
                } else {
                    it.split(",").mapNotNull {
                        val trimmed = it.trim()
                        if (trimmed.isBlank() || trimmed.length < 3) null else trimmed
                    }
                }
            },
            onLangkahChange = {
                langkah = it
            },
            modifier = Modifier.padding(padding),

        )

        if (id != null && showDialog) {
            DisplayAlertDialog(
                message = context.getString(R.string.pesan_hapus),
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
fun FormResep(judul: String, kategori: String, bahan: List<String>, langkah: String,
              onJudulChange: (String) -> Unit, onKategoriChange: (String) -> Unit,
              onBahanChange: (String) -> Unit, onLangkahChange: (String) -> Unit,
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
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Bahanlist(bahan, onBahanChange)

        OutlinedTextField(
            value = langkah,
            onValueChange = {
                onLangkahChange(it)
            },
            label = { Text(text = stringResource(R.string.langkah)) },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Bahanlist(bahan: List<String>, onBahanChange: (String) -> Unit) {
    val context = LocalContext.current
    val bahanList = remember { mutableStateListOf<String>() }
    var newBahan by remember { mutableStateOf("") }

    if (bahan.isNotEmpty()) {
        bahanList.clear()
        bahanList.addAll(bahan)
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        OutlinedTextField(
            value = newBahan,
            onValueChange = {
                newBahan = it
            },
            label = { Text(text = stringResource(R.string.bahan)) },
            singleLine = true,
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Done,

            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (tambahBahan(context, bahanList, newBahan)) {
                        newBahan = ""
                        onBahanChange(bahanList.joinToString(","))
                    }
                }
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                if (tambahBahan(context, bahanList, newBahan)) {
                    newBahan = ""
                    onBahanChange(bahanList.joinToString(","))
                }
            },
            modifier = Modifier.size(56.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }
    }

    if (bahanList.isNotEmpty()) {
        Column(
            Modifier.heightIn(0.dp, 200.dp).verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.bahan),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                bahanList.forEach { bahans ->
                    FilterChip(
                        selected = true,
                        onClick = {
                            bahanList.remove(bahans)
                            if (bahanList.isEmpty()) {
                                onBahanChange("")
                            } else {
                                onBahanChange(bahanList.joinToString(","))
                            }
                        },
                        label = { Text(bahans) },
                        trailingIcon = {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = stringResource(R.string.hapus_bahan),
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                            selectedLeadingIconColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    )
                }
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.bahan_masih_kosong),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

private fun tambahBahan(context: Context, list: MutableList<String>, item: String): Boolean {
    if (item.isBlank()) {
        Toast.makeText(context, R.string.bahan_kosong, Toast.LENGTH_LONG).show()
        return false
    } else if (item.length < 3) {
        Toast.makeText(context, R.string.bahan_min3, Toast.LENGTH_LONG).show()
        return false
    }

    if (list.contains(item)) {
        Toast.makeText(context, R.string.bahan_sudah_ada, Toast.LENGTH_LONG).show()
        return false
    }

    list.add(item)
    return true
}

@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {
    ThemeController {
        EditScreen(rememberNavController())
    }
}