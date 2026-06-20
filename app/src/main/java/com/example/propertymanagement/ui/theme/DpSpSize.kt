package com.example.propertymanagement.ui.theme

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ui/theme/Dimens.kt

/** Нулевая толщина / отсутствие отступа в dp */
val DpZero = 0.dp

/** Нулевой letterSpacing и т.п. */
val SpZero = 0.sp

/** Тонкая обводка (полупиксель на mdpi-логике) */
val HairlineBorderWidth = 0.5.dp

/** Скругление 2dp (ручка bottom sheet, акценты) */
val CornerRadiusExtraSmall = 2.dp

/** Промежуток 12dp между элементами в ряду */
val Spacing12 = 12.dp

/** Вертикальный отступ вокруг разделителя в bottom sheet */
val SpacerHeightSection = 14.dp

/** Плотный вертикальный spacer (оценка приложения и т.д.) */
val SpacerHeightTight = 6.dp

/** Минимальный размер зоны нажатия (Material) */
val TouchTargetMinimum = 48.dp

/** Кнопка фильтра в TopBar списка */
val TopBarEndIconBoxSize = 48.dp

/** Высота ряда чипов фильтров на карте */
val MapFilterRowHeight = 48.dp

/** Верхние углы bottom sheet */
val BottomSheetTopCornerRadius = 24.dp

/** Отступ зоны drag-handle у bottom sheet */
val BottomSheetDragHandlePaddingVertical = 12.dp

/** Ширина полоски «ручки» bottom sheet */
val BottomSheetDragHandleWidth = 40.dp

/** Контейнер эмодзи / главная кнопка в sheet оценки */
val RateAppEmojiContainerSize = 56.dp

/** Иконка звезды в оценке и на карточке маркера */
val IconSizeStarRow = 22.dp

/** Обводка звезды (Canvas) */
val StarStrokeWidth = 2.dp

/** Кнопка «Отправить» в sheet оценки */
val RateAppSubmitButtonSize = 52.dp

/** Квадратные кнопки 40dp (FAB на карте, действие в picker) */
val IconSizeActionSquare = 40.dp

/** Лёгкий тональный подъём Surface */
val SurfaceTonalElevationLow = 1.dp

/** Горизонтальные отступы бейджа модерации */
val ModerationBadgePaddingHorizontal = 10.dp

// Typography (Material Type + экраны)
val TypeBodyLineHeight = 24.sp
val TypeTitleFontSize = 22.sp
val TypeTitleLineHeight = 28.sp
val TypeLabelFontSize = 11.sp
val TypeLabelLineHeight = 16.sp
val TypeLetterSpacingTight = 0.5.sp

/** Имя на экране профиля */
val ProfileDisplayNameFontSize = 28.sp

val ProfileHeaderAvatarSize = 76.dp

/** Карточки на экране профиля / личных данных */
val ProfileSectionCardElevation = 2.dp

/** Иконки в пунктах меню профиля */
val ProfileMenuIconContainerSize = 44.dp

// BottomBar
val BottomBarIconSize = 28.dp

val BottomBarVerticalPadding = 8.dp

// Text
val BottomBarTextSize = 11.5.sp
val BottomBarTextLineHeight = 12.sp

// Отступы (Padding / Spacer)
val PaddingSmall = 4.dp
val PaddingMedium = 8.dp
val PaddingLarge = 16.dp
val PaddingExtraLarge = 24.dp

// Высота Spacer'ов
val SpacerSmall = 8.dp
val SpacerMedium = 16.dp
val SpacerLarge = 24.dp
val SpacerExtraLarge = 32.dp


// Новый Spacer для мелких промежутков (4.dp в CategoryItem)
val SpacerTiny = 4.dp

// Размеры иконок
val IconSmall = 18.dp
val IconMedium = 26.dp

// Радиусы
val ChipCornerRadius = 20.dp
val ButtonCornerRadius = 12.dp

// TopBar и Category
val TopBarHeight = 56.dp
val HorizontalPadding = 24.dp
val SmallHorizontalPadding = 4.dp
val IconSizeCategory = 28.dp
val IconSizeArrow = 24.dp
val SpacerBetweenElements = 16.dp
val VerticalPaddingCategory = 20.dp
val DividerThickness = 1.dp

// Новый padding для CategoryItem (16.dp)
val VerticalPaddingItem = 16.dp
val VerticalPaddingItemSmall = 8.dp

val HeightOutlinedTextField = 46.dp
val HeightFilterChip = 46.dp

// TextField
val TextFieldHorizontalPadding = 12.dp
val TextFieldVerticalPadding = 8.dp
val TextFieldBorderWidth = 1.dp
val TextFieldPrefixSpacing = 4.dp

val BoxGrayHeight = 24.dp
val BoxGrayHeightSettings = 44.dp

// WheelPicker
val WheelPickerItemHeight = 66.dp
val WheelPickerVisibleItems = 3
val WheelPickerLineWidth = 240.dp
val WheelPickerLineThickness = 1.dp
val WheelPickerLetterSpacing = 0.15.sp


val ImagePickerHeight = 200.dp

/** Превью карты на экране объекта (высота прямоугольника) */
val PropertyDetailMapPreviewHeight = ImagePickerHeight

/** Скругление превью карты */
val PropertyDetailMapCornerRadius = 16.dp

/** Иконка категории POI в строке чекбоксов на карте объекта (= [MapSizesColors.POI_CATEGORY_ICON_DP]). */
val MapPoiCategoryCheckboxIconSize = MapSizesColors.POI_CATEGORY_ICON_DP.dp

/** Компактная иконка в чипе слоя POI на карте */
val MapPoiLayerChipIconSize = 18.dp

/** Высота чипа слоя POI */
val MapPoiLayerChipHeight = 36.dp

/** Скругление чипа слоя POI */
val MapPoiLayerChipCornerRadius = 20.dp

/** Круг под иконкой в плитке слоя POI */
val MapPoiLayerTileIconCircleSize = 46.dp

/** Иконка внутри круга плитки POI */
val MapPoiLayerTileIconSize = 22.dp

/** Индикатор выбранной плитки POI снизу */
val MapPoiLayerTileAccentHeight = 3.dp

/** Скругление панели слоёв / статуса POI */
val MapPoiLayersPanelCornerRadius = 16.dp

/** Отступ снизу для FAB «Моё местоположение» над панелью слоёв POI */
val MapPoiFullscreenBottomPanelInset = 118.dp
val MapRouteFullscreenBottomSheetInset = 220.dp

/** Соотношение сторон галереи фото на экране объекта */
const val PropertyDetailHeroAspectRatio = 4f / 3f

/** Elevation для Surface (превью карты и др.) */
val CardElevationLow = 2.dp

val IndicatorSize = 6.dp
val IndicatorSizeActive = 8.dp
val IconPassword = 20.dp

val TextLarge = 18.sp
val TextMedium = 16.sp
val TextRegular = 14.sp
val TextSmall = 12.sp

/** Строки параметров на экране объекта (чуть крупнее стандартного body) */
val PropertyDetailInfoLabelFontSize = 15.sp
val PropertyDetailInfoLabelLineHeight = 20.sp
val PropertyDetailInfoValueFontSize = 17.sp
val PropertyDetailInfoValueLineHeight = 22.sp

val AvatarSize = 64.dp
val IconSizeArrowLarge = 32.dp
val IconSizeProfile = 24.dp

val imageItem = 100.dp

val AvatarLargeSize = 90.dp
val AvatarLargeIconSize = 48.dp

val FavoriteIconSize = 50.dp


