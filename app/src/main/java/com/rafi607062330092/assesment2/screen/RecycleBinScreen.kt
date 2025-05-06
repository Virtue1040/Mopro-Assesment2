package com.rafi607062330092.assesment2.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.rafi607062330092.assesment2.R
import com.rafi607062330092.assesment2.database.ResepDb
import com.rafi607062330092.assesment2.model.Resep
import com.rafi607062330092.assesment2.navigation.Screen
import com.rafi607062330092.assesment2.ui.theme.ThemeController
import com.rafi607062330092.assesment2.util.ViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecycleBinScreen(navController: NavHostController) {
    val context = LocalContext.current
    val db = ResepDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModelDetail: DetailViewModel = viewModel(factory = factory)
    val viewModel: MainViewModel = viewModel(factory = factory)
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            id = R.string.recycle_bin
                        )
                    )

                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    ActionAll(
                        restoreAll = {
                            viewModelDetail.undoAll()
                            Toast.makeText(context,
                                context.getString(R.string.semua_resep_restored),
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        deleteAll = {
                            showDialog = true
                        }
                    )
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
        },
    ) { padding ->
        ScreenContent(Modifier.padding(padding), viewModelDetail, viewModel, navController)

        if (showDialog) {
            DisplayAlertDialog(
                message = context.getString(R.string.pesan_hapus_semua_permanent),
                onDismissRequest = {
                    showDialog = false
                },
                onConfirmation = {
                    showDialog = false
                    viewModelDetail.hardDeleteAll()
                    Toast.makeText(context,
                        context.getString(R.string.semua_resep_deleted),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            )
        }
    }
}

@Composable
private fun ActionItem(restore: () -> Unit, delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedIconButton (
        modifier = Modifier.fillMaxSize().height(100.dp),
        border = null,
        onClick = {
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
                        text = stringResource(id = R.string.restore)
                    )
                },
                onClick = {
                    expanded = false
                    restore()
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
private fun ActionAll(restoreAll: () -> Unit, deleteAll: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton (
        onClick = {
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
                        text = stringResource(id = R.string.restore_semua)
                    )
                },
                onClick = {
                    expanded = false
                    restoreAll()
                }
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.hapus_semua)
                    )
                },
                onClick = {
                    expanded = false
                    deleteAll()
                }
            )
        }
    }
}

@Composable
private fun ListItem(onClick: () -> Unit, resep: Resep, onClickRestore: () -> Unit, onClickDelete: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(16.dp).weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = resep.judul,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = resep.kategori,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = resep.tanggal
                )
            }
            Column(
                modifier = Modifier.padding(end = 8.dp).width(40.dp),
            ) {
                ActionItem(
                    restore = {
                        onClickRestore()
                    },
                    delete = {
                        onClickDelete()
                    }
                )
            }
        }

    }
}

@Composable
private fun ScreenContent(modifier: Modifier, viewModelDetail: DetailViewModel, viewModel: MainViewModel, navController: NavHostController) {
    val context = LocalContext.current
    val data by viewModel.dataDeleted.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var id by remember { mutableStateOf(0L) }

    if (data.isEmpty()) {
        Column (
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.list_kosong_recycle_bin)
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 84.dp),
        ) {
            items(data) {
                ListItem(resep = it,
                    onClick = {
                        navController.navigate(Screen.FormDetail.withId(it.id))
                    },
                    onClickDelete = {
                        showDialog = true
                        id = it.id
                    },
                    onClickRestore = {
                        viewModelDetail.undo(it.id)
                        Toast.makeText(context,
                            context.getString(R.string.resep_restored),
                            Toast.LENGTH_SHORT
                        ).show()
                    })
                HorizontalDivider()
            }
        }
    }

    if (showDialog) {
        DisplayAlertDialog(
            message = context.getString(R.string.pesan_hapus_permanent),
            onDismissRequest = {showDialog = false},
            onConfirmation = {
                showDialog = false
                viewModelDetail.hardDelete(id)
                Toast.makeText(context,
                    context.getString(R.string.resep_deleted),
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RecyclePreview() {
    ThemeController {
        RecycleBinScreen(rememberNavController())
    }
}
