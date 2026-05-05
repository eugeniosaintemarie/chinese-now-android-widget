# Plan de port a iOS para Chinese Now

## Objetivo
Crear una app minima de iOS con una extension WidgetKit que replique el widget actual en contenido, estilo y zona horaria, portando la logica de conversion a Swift.

## Nota de entorno
El proyecto iOS real debe crearse en Xcode sobre macOS. Desde este workspace solo podemos dejar el plan, la estructura y el contenido de los archivos Swift; el `.xcodeproj`, los targets y la firma se configuran dentro de Xcode.

## Alcance
- App contenedora minima
- Extension WidgetKit
- Logica compartida en Swift para fechas, numeros, dias, meses, ano y estacion
- UI en SwiftUI equivalente al layout Android
- Validacion de paridad contra el widget actual

## Decisiones clave
- Mantener la zona horaria fija `America/Argentina/Buenos_Aires`
- Replicar el contenido actual: hora, dia, mes, ano y estacion, con pinyin
- Asumir las limitaciones de iOS para refresco: usar timeline en iOS 16 o interactividad en iOS 17+
- Soportar primero familias medianas y grandes; la familia pequena se decide aparte

## Fases de implementacion
1. Crear el proyecto base en Xcode con target de app y target de widget.
2. Definir el modulo compartido Swift para la logica de conversion.
3. Construir la vista del widget en SwiftUI con la misma jerarquia visual del Android.
4. Definir el modelo de actualizacion con `TimelineProvider` y, si aplica, interaccion para refresco.
5. Crear una app minima de apoyo con acceso a la web actual o una pantalla de ayuda.
6. Añadir pruebas y validar que la salida coincida con fechas de referencia del Android.

## Arranque en Xcode
1. En Xcode, crear un nuevo proyecto iOS App con interfaz SwiftUI.
2. Agregar un Widget Extension al mismo proyecto.
3. Elegir el deployment target segun el nivel de interactividad deseado: iOS 17+ si se quiere accion de refresh, iOS 16 si se acepta solo timeline.
4. Crear una carpeta compartida para la logica de conversion y otra para las vistas del widget.
5. Portar primero el servicio de fechas y textos, luego la UI, y al final la app contenedora minima.
6. Validar el widget en preview y en simulador antes de pensar en publicar.

## Estructura exacta de Xcode
Esta es la estructura recomendada para arrancar el proyecto en Xcode:

```text
ChineseNow/
├─ ChineseNowApp.swift
├─ ContentView.swift
├─ Shared/
│  ├─ ChineseDateService.swift
│  ├─ ChineseModels.swift
│  └─ ChineseFormatting.swift
├─ Widget/
│  ├─ ChineseNowWidgetBundle.swift
│  ├─ ChineseNowWidget.swift
│  ├─ ChineseNowEntry.swift
│  ├─ ChineseNowProvider.swift
│  ├─ ChineseNowWidgetView.swift
│  └─ WidgetAssets.xcassets
├─ Resources/
│  └─ PreviewContent/
└─ Tests/
	└─ ChineseDateServiceTests.swift
```

## Orden de implementacion en Xcode
1. Crear `ChineseNowApp.swift` y `ContentView.swift` para que el target de app compile.
2. Crear `ChineseDateService.swift` con la logica portada desde `ChineseConverter.kt`.
3. Crear `ChineseModels.swift` para el modelo de datos que consume el widget.
4. Crear `ChineseNowProvider.swift` y `ChineseNowEntry.swift` para el timeline.
5. Crear `ChineseNowWidgetView.swift` para la UI en SwiftUI.
6. Crear `ChineseNowWidget.swift` y `ChineseNowWidgetBundle.swift` para registrar la extension.
7. Añadir `ChineseDateServiceTests.swift` para comparar salidas con casos de referencia.
8. Refinar `ContentView.swift` para que sea una pantalla minima de ayuda o enlace.

## Archivos de referencia del Android actual
- `app/src/main/java/com/example/chinesenowwidget/ChineseConverter.kt`
- `app/src/main/java/com/example/chinesenowwidget/ChineseNowWidget.kt`
- `app/src/main/res/layout/widget_layout.xml`
- `app/src/main/res/xml/widget_info.xml`
- `app/src/main/java/com/example/chinesenowwidget/WidgetWebViewActivity.kt`

## Criterios de validacion
- Los textos generados deben coincidir con Android para fechas de prueba conocidas.
- El widget debe verse legible en las familias soportadas.
- La timezone y la logica de estaciones deben mantenerse iguales al proyecto actual.
- El refresh no debe depender de un comportamiento que iOS no garantice.

## Siguiente paso
Definir la estructura exacta de archivos de Xcode y empezar por el servicio Swift de conversion.
