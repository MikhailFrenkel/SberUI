package com.example.jetpackcompose

import android.opengl.Visibility
import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.jetpackcompose.model.*
import com.example.jetpackcompose.ui.theme.BodyBgColor
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme
import com.example.jetpackcompose.ui.theme.Lexem
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeTheme {
                SberScreen()
            }
        }
    }
}

@Composable
fun SberScreen() {
    val allScreens = SberTab.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentTab = SberTab.fromRoute(
        backstackEntry.value?.destination?.route
    )
    Column {
        val scrollState = rememberScrollState()
        Toolbar(
            scrollState = scrollState,
            tabs = allScreens,
            onTabSelected = { navController.navigate(it.name) },
            currentTab = currentTab
        )
        NavHost(
            navController = navController,
            startDestination = SberTab.Profile.name
        ) {
            composable(SberTab.Profile.name) {
                ProfileBody(scrollState)
            }

            composable(SberTab.Settings.name) {
                SettingsBody(scrollState)
            }
        }
    }
}

@Composable
private fun ProfileBody(
    scrollState: ScrollState
) {
    Surface(
        color = BodyBgColor
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(state = scrollState)
        ) {
            Section(
                title = Lexem.Title1,
                subTitle = Lexem.Subtitle1,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 30.dp,
                    end = 16.dp
                )
            )

            Subscriptions()

            Section(
                title = Lexem.Title2,
                subTitle = Lexem.Subtitle2,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 24.dp,
                    end = 16.dp
                )
            )

            Operations(
                modifier = Modifier.padding(top = 24.dp)
            )

            Section(
                title = Lexem.Title3,
                subTitle = Lexem.Subtitle3,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 30.dp,
                    end = 16.dp
                )
            )

            Chips(Modifier.padding(16.dp))
        }
    }
}

@Composable
private fun Chips(
    modifier: Modifier = Modifier
) {
    ChipsGroup(
        modifier = modifier
    ) {
        chips.forEach {
            ChipItem(item = it)
        }
    }
}

@Composable
private fun ChipsGroup(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        val coordinates: List<Coordinate> = List(measurables.size) {
            Coordinate()
        }
        var height: Int = 0
        var x: Int = 0

        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)

            x = coordinates[index].x
            if (x + placeable.width >= constraints.maxWidth) {
                height += placeable.height
                coordinates[index].x = 0
                x = 0
            }

            if (index + 1 < measurables.size) {
                coordinates[index + 1].x = x + placeable.width
            }

            coordinates[index].y = height

            if (index == measurables.lastIndex) {
                height += placeable.height
            }

            placeable
        }

        layout(constraints.maxWidth, height) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = coordinates[index].x,
                    y = coordinates[index].y
                )
            }
        }
    }
}

@Composable
private fun ChipItem(item: Chip) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.Black.copy(alpha = 0.08f),
        modifier = Modifier
            .padding(4.dp)
            .height(32.dp)
    ) {
        Text(
            text = item.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.W500,
            lineHeight = 18.sp,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 7.dp, horizontal = 12.dp)
        )
    }
}

@Composable
private fun Operations(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        operations.forEachIndexed { index, item ->
            OperationItem(item, index == operations.lastIndex)
        }
    }
}

@Composable
private fun OperationItem(
    item: Operation,
    isLast: Boolean = false
) {
    ConstraintLayout(
        modifier = Modifier
            .padding(vertical = 6.dp)
            .fillMaxWidth()
    ) {
        val guidelineOffset = 36.dp + 16.dp + 16.dp
        val guideline = createGuidelineFromStart(guidelineOffset)
        val (img, title, subtitle, arrow, divider) = createRefs()
        Image(
            painter = painterResource(id = item.imgResource),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 16.dp)
                .size(36.dp, 40.dp)
                .constrainAs(img) {
                    top.linkTo(title.top)
                    bottom.linkTo(if (item.subtitle.isNullOrEmpty()) parent.bottom else subtitle.bottom)
                    start.linkTo(parent.start)
                }
        )

        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.W500,
            lineHeight = 20.sp,
            color = Color.Black,
            modifier = Modifier
                .constrainAs(title) {
                    width = Dimension.fillToConstraints
                    top.linkTo(parent.top)
                    start.linkTo(guideline)
                    end.linkTo(arrow.start, 16.dp)
                }
        )
        if (!item.subtitle.isNullOrEmpty()) {
            Text(
                text = item.subtitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.W500,
                lineHeight = 18.sp,
                color = Color.Black.copy(alpha = 0.55f),
                modifier = Modifier.constrainAs(subtitle) {
                    width = Dimension.fillToConstraints
                    top.linkTo(title.bottom, 2.dp)
                    start.linkTo(guideline)
                    end.linkTo(arrow.start, 16.dp)
                }
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_right_arrow),
            contentDescription = null,
            modifier = Modifier
                .size(8.dp, 12.dp)
                .constrainAs(arrow) {
                    top.linkTo(title.top)
                    bottom.linkTo(if (isLast) parent.bottom else subtitle.bottom)
                    end.linkTo(parent.end, 16.dp)
                }
        )
        if (!isLast) {
            Divider(
                color = Color.Black.copy(alpha = 0.12f),
                modifier = Modifier.constrainAs(divider) {
                    width = Dimension.fillToConstraints
                    start.linkTo(guideline)
                    top.linkTo(subtitle.bottom, 12.dp)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
            )
        }
    }
}

@Composable
private fun Subscriptions(
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier.padding(start = 16.dp)
    ) {
        items(subscriptions) { item ->
            SubscriptionItem(item)
        }
    }
//    val scrollState = rememberScrollState()
//    Row(
//        modifier
//            .horizontalScroll(scrollState)
//            .padding(start = 16.dp)
//        ) {
//        subscriptions.forEach { item ->
//            SubscriptionItemView(item)
//        }
//    }
}

@Composable
private fun SubscriptionItem(
    item: Subscription
) {
    Card(
        backgroundColor = Color.White,
        elevation = 8.dp,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(horizontal = 4.dp, vertical = 20.dp)
            .width(216.dp)
            .height(130.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = item.imgResource),
                    contentDescription = null,
                    modifier = Modifier.size(36.dp, 40.dp)
                )
                Text(
                    text = item.title,
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Column {
                Text(
                    text = item.text,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.subtext,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.W500,
                    lineHeight = 18.sp,
                    color = Color.Black.copy(alpha = 0.55f)
                )
            }
        }
    }
}

@Composable
private fun Section(
    title: String,
    subTitle: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 20.sp,
            lineHeight = 24.sp,
            fontWeight = FontWeight.W700,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subTitle,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontWeight = FontWeight.W500,
            color = Color.Black.copy(alpha = 0.55f)
        )
    }
}

@Composable
private fun SettingsBody(
    scrollState: ScrollState
) {
    Surface(
        color = BodyBgColor
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(Lexem.Settings)
        }
    }
}

@Composable
private fun Tabs(
    tabs: List<SberTab>,
    onTabSelected: (SberTab) -> Unit,
    currentTab: SberTab
) {
    val selectedTabIndex = tabs.indexOf(currentTab)
    TabRow(
        selectedTabIndex = selectedTabIndex,
        backgroundColor = Color.White,
        indicator = @Composable { tabPositions ->
            TabRowDefaults.Indicator(
                modifier = Modifier
                    .height(4.dp)
                    .tabIndicatorOffset(tabPositions[selectedTabIndex]),
                color = Color(0xFF068441)
            )
        }
    ) {
        tabs.forEachIndexed { index, sberTab ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onTabSelected(sberTab) },
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Black.copy(alpha = 0.55f)
            ) {
                Text(
                    text = sberTab.title,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.W500,
                    modifier = Modifier.padding(vertical = 16.dp))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun Toolbar(
    scrollState: ScrollState,
    tabs: List<SberTab>,
    onTabSelected: (SberTab) -> Unit,
    currentTab: SberTab
) {

    val profileImageShape = RoundedCornerShape(38.dp)
    Card(
        elevation = 8.dp,
        shape = RectangleShape
    ) {
        Column{
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clear),
                    contentDescription = null,
                    modifier = Modifier
                        .width(18.dp)
                        .height(16.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.profile_image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp)
                        .shadow(16.dp, profileImageShape)
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_exit),
                    contentDescription = null,
                    modifier = Modifier
                        .width(18.dp)
                        .height(16.dp)
                )
            }
            Text(
                text = Lexem.Name,
                color = Color.Black,
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            )

            Tabs(
                tabs = tabs,
                onTabSelected = onTabSelected,
                currentTab = currentTab
            )
        }
    }
}


@Composable
fun AppBar() {
    val scrollState = rememberScrollState()
    // parallax effect by offset
    val imageOffset = (-scrollState.value * 0.18f).dp
    Box {
        Image(
            painter = painterResource(id = R.drawable.ic_clock),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .graphicsLayer { translationY = -scrollState.value * 0.18f }
                .height(240.dp)
                .fillMaxWidth()
        )

        Column(
            Modifier
                .verticalScroll(scrollState)
                .padding(top = 200.dp)
                .background(
                    MaterialTheme.colors.surface,
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(all = 16.dp)
        ) {
            Text(Lexem.LoremIpsum)
        }
    }
}

@Composable
fun CollapsingToolbar() {
    val toolbarHeight = 48.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {

                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)
                return Offset.Zero
            }
        }
    }
    Box(
        Modifier
            .fillMaxSize()
            .nestedScroll(nestedScrollConnection)
    ) {

        LazyColumn(contentPadding = PaddingValues(top = toolbarHeight)) {
            items(100) { index ->
                Text("I'm item $index", modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp))
            }
        }
        TopAppBar(
            modifier = Modifier
                .height(toolbarHeight)
                .offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) },
            title = { Text("toolbar offset is ${toolbarOffsetHeightPx.value}") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackComposeTheme {
        SberScreen()
    }
}