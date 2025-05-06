package com.rafi607062330092.assesment2.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.rafi607062330092.assesment2.R
import com.rafi607062330092.assesment2.database.ResepDb
import com.rafi607062330092.assesment2.navigation.Screen
import com.rafi607062330092.assesment2.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavController, id: Long) {
    val context = LocalContext.current
    val db = ResepDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var judul by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var bahan by remember { mutableStateOf(listOf<String>()) }
    var langkah by remember { mutableStateOf("") }
    var tanggal by remember { mutableStateOf("") }
    var isDeleted by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        val data = viewModel.getResep(id) ?: return@LaunchedEffect
        judul = data.judul
        kategori = data.kategori
        bahan = data.bahan
        langkah = data.langkah
        tanggal = data.tanggal
        isDeleted = data.isDelete
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.resep) + " " + judul,
                    )
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if (!isDeleted) {
                        Action(
                            edit = {
                                navController.navigate(Screen.FormUbah.withId(id))
                            },
                            delete = {
                                showDialog = true
                            }
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
            tanggal = tanggal,
            modifier = Modifier.padding(padding)
        )

        if (showDialog) {
            DisplayAlertDialog(
                message = context.getString(R.string.pesan_hapus),
                onDismissRequest = {
                    showDialog = false
                }
            ) {
                showDialog = false
                viewModel.delete(id)
                navController.navigate(Screen.HomeArgument.withId(id))
            }
        }
    }
}

@Composable
fun Action(edit: () -> Unit, delete: () -> Unit) {
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
                        text = stringResource(id = R.string.ubah_resep)
                    )
                },
                onClick = {
                    expanded = false
                    edit()
                }
            )
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
fun Detail(
    title: String,
    value: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.small
            ).padding(8.dp)
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
        ) {
            value()
        }
    }
}

@Composable
fun BahanChip(title: String) {
    Column(
        modifier = Modifier.background(
            MaterialTheme.colorScheme.secondaryContainer,
            shape = MaterialTheme.shapes.small
        ).padding(8.dp),
    ) {
        Text(
            text = title,

        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FormResep(
    judul: String,
    kategori: String,
    bahan: List<String>,
    langkah: String,
    tanggal: String,
    modifier: Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 84.dp)
    ) {
        item {
            Detail(
                title = stringResource(R.string.judul) + " " + stringResource(R.string.resep),
                value = {
                    Text(
                        text = judul,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }

        item {
            Detail(
                title = stringResource(R.string.kategori),
                value = {
                    Text(
                        text = kategori,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }
        item {
            Detail(
                title = stringResource(R.string.tanggal),
                value = {
                    Text(
                        text = tanggal,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }
        item {
            HorizontalDivider()
        }
        item {
            Detail(
                title = stringResource(R.string.bahan),
                value = {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        bahan.forEach { item ->
                            BahanChip(item)
                        }
                    }
                }
            )
        }
        item {
            Detail(
                title = stringResource(R.string.langkah),
                value = {
                    Text(
                        text = langkah,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            )
        }
    }
}

