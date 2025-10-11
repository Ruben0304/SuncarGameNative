package com.suncar.solarsurvivor.data

object SituationsDatabase {

    private val easySituations = listOf(
        Situation(
            id = "easy_1",
            title = "Cálculo de Autonomía Básico",
            description = "Tienes 2 baterías de 5kWh cada una cargadas al 80%. Tu consumo actual es de 400W. ¿Cuántas horas de autonomía tienes aproximadamente?",
            difficulty = SituationDifficulty.EASY,
            options = listOf(
                SituationOption(
                    text = "20 horas",
                    isCorrect = true,
                    explanation = "Correcto. Capacidad disponible: 2 × 5kWh × 0.8 = 8kWh = 8000Wh. Con consumo de 400W: 8000Wh ÷ 400W = 20 horas de autonomía."
                ),
                SituationOption(
                    text = "25 horas",
                    isCorrect = false,
                    explanation = "Incorrecto. No consideraste que las baterías están al 80%, no al 100%. El cálculo correcto: (2×5kWh×0.8) ÷ 0.4kW = 20h."
                ),
                SituationOption(
                    text = "16 horas",
                    isCorrect = false,
                    explanation = "Incorrecto. Parece que calculaste solo con una batería. Son 2 baterías de 5kWh, dando 10kWh total al 80% = 8kWh disponibles."
                ),
                SituationOption(
                    text = "10 horas",
                    isCorrect = false,
                    explanation = "Incorrecto. Subestimaste la capacidad. Con 8kWh disponibles (2×5kWh×80%) y 400W de consumo, la autonomía es mucho mayor."
                )
            ),
            points = 15
        ),
        Situation(
            id = "easy_2",
            title = "Eficiencia del Inversor",
            description = "Tu inversor tiene 92% de eficiencia. Los paneles generan 3000W DC. ¿Cuántos watts AC reales obtienes?",
            difficulty = SituationDifficulty.EASY,
            options = listOf(
                SituationOption(
                    text = "2760W AC",
                    isCorrect = true,
                    explanation = "Correcto. 3000W × 0.92 = 2760W. El inversor pierde 8% en la conversión DC→AC, dejando 92% de potencia utilizable."
                ),
                SituationOption(
                    text = "3000W AC",
                    isCorrect = false,
                    explanation = "Incorrecto. La conversión DC a AC siempre tiene pérdidas. Tu inversor al 92% de eficiencia entrega solo 2760W AC."
                ),
                SituationOption(
                    text = "2400W AC",
                    isCorrect = false,
                    explanation = "Incorrecto. Calculaste con 80% de eficiencia, pero el inversor tiene 92%. El resultado correcto es 3000W × 0.92 = 2760W."
                ),
                SituationOption(
                    text = "3260W AC",
                    isCorrect = false,
                    explanation = "Incorrecto. Los inversores no generan más energía de la que reciben. Pierden eficiencia, no la ganan. 92% significa que obtienes menos, no más."
                )
            ),
            points = 15
        ),
        Situation(
            id = "easy_3",
            title = "Dimensionamiento de Paneles",
            description = "Tu consumo diario promedio es 12kWh. Considerando 5 horas solares pico, ¿cuántos kW de paneles necesitas mínimo (sin considerar pérdidas)?",
            difficulty = SituationDifficulty.EASY,
            options = listOf(
                SituationOption(
                    text = "2.4 kW",
                    isCorrect = true,
                    explanation = "Correcto. 12kWh ÷ 5 horas = 2.4kW necesarios. Esto es el mínimo teórico. En práctica necesitarías más por pérdidas y días nublados."
                ),
                SituationOption(
                    text = "12 kW",
                    isCorrect = false,
                    explanation = "Incorrecto. Confundiste kWh (energía) con kW (potencia). No necesitas 12kW de paneles, sino suficiente para generar 12kWh en 5 horas."
                ),
                SituationOption(
                    text = "5 kW",
                    isCorrect = false,
                    explanation = "Incorrecto. Con 5kW generarías 25kWh al día (5kW × 5h), el doble de lo necesario. El cálculo es: consumo diario ÷ horas pico."
                ),
                SituationOption(
                    text = "1.2 kW",
                    isCorrect = false,
                    explanation = "Incorrecto. Eso solo generaría 6kWh al día (1.2kW × 5h), la mitad de lo que necesitas. Revisa el cálculo: 12kWh ÷ 5h = 2.4kW."
                )
            ),
            points = 15
        ),
        Situation(
            id = "easy_4",
            title = "Priorización en Apagón",
            description = "Apagón de 6h, batería al 40% (2kWh disponibles). Debes elegir qué mantener encendido: ¿Refrigerador (150W) + Luces (60W) + Router (25W) o AC (1500W)?",
            difficulty = SituationDifficulty.EASY,
            options = listOf(
                SituationOption(
                    text = "Refrigerador + Luces + Router",
                    isCorrect = true,
                    explanation = "Correcto. Consumo total: 235W × 6h = 1.41kWh, dentro de tu capacidad (2kWh). AC solo duraría 1.3h y no es esencial."
                ),
                SituationOption(
                    text = "Solo el Aire Acondicionado",
                    isCorrect = false,
                    explanation = "Incorrecto. AC consume 1500W, agotaría tus 2kWh en 1.3 horas. Perderías alimentos y estarías sin luz las otras 4.7 horas."
                ),
                SituationOption(
                    text = "Todo encendido",
                    isCorrect = false,
                    explanation = "Incorrecto. El consumo combinado (1735W) agotaría tu batería en 1.15 horas. Debes priorizar equipos esenciales de bajo consumo."
                ),
                SituationOption(
                    text = "Solo el refrigerador",
                    isCorrect = false,
                    explanation = "Muy conservador. Tienes capacidad para más. Con 2kWh puedes mantener refrigerador + luces + router durante las 6 horas sin problema."
                )
            ),
            points = 15
        ),
        Situation(
            id = "easy_5",
            title = "Tipos de Baterías",
            description = "Necesitas reemplazar baterías. ¿Cuál es la ventaja principal de litio sobre AGM/plomo-ácido para sistemas solares residenciales?",
            difficulty = SituationDifficulty.EASY,
            options = listOf(
                SituationOption(
                    text = "Mayor profundidad de descarga (80-90% vs 50%) y más ciclos de vida",
                    isCorrect = true,
                    explanation = "Correcto. Litio permite usar 80-90% de capacidad vs 50% en AGM, dura 3000-5000 ciclos vs 500-1000, aunque cuesta más inicialmente."
                ),
                SituationOption(
                    text = "Son más baratas",
                    isCorrect = false,
                    explanation = "Incorrecto. Las baterías de litio son 2-3 veces más caras inicialmente. Su ventaja está en rendimiento, vida útil y eficiencia."
                ),
                SituationOption(
                    text = "No necesitan inversor",
                    isCorrect = false,
                    explanation = "Incorrecto. Todas las baterías DC necesitan inversor para alimentar cargas AC. El tipo de batería no cambia esta necesidad."
                ),
                SituationOption(
                    text = "Generan más electricidad",
                    isCorrect = false,
                    explanation = "Incorrecto. Las baterías no generan electricidad, solo la almacenan. Los paneles solares son los que generan la energía."
                )
            ),
            points = 15
        )
    )

    private val mediumSituations = listOf(
        Situation(
            id = "medium_1",
            title = "ROI Solar Complejo",
            description = "Sistema solar: $8000 (4kW paneles + 10kWh baterías). Ahorras $85/mes en electricidad. Hay 3 apagones/semana de 4h c/u. Sin solar gastarías $40/mes adicional en gasolina para generador y comida perdida. ¿En cuántos años recuperas la inversión?",
            difficulty = SituationDifficulty.MEDIUM,
            options = listOf(
                SituationOption(
                    text = "5.3 años",
                    isCorrect = true,
                    explanation = "Correcto. Ahorro total mensual: $85 (electricidad) + $40 (generador/comida) = $125/mes = $1500/año. ROI: $8000 ÷ $1500 = 5.33 años."
                ),
                SituationOption(
                    text = "7.8 años",
                    isCorrect = false,
                    explanation = "Incorrecto. Solo consideraste el ahorro de electricidad ($85/mes). Debes sumar también los $40/mes ahorrados en generador y comida perdida."
                ),
                SituationOption(
                    text = "16.6 años",
                    isCorrect = false,
                    explanation = "Incorrecto. Parece que solo calculaste con el ahorro extra de $40/mes. El ahorro total es $125/mes ($85+$40), no solo $40."
                ),
                SituationOption(
                    text = "3.2 años",
                    isCorrect = false,
                    explanation = "Incorrecto. Sobrestimaste el ahorro. Con $125/mes de ahorro real, recuperas $8000 en 5.3 años, no 3.2."
                )
            ),
            points = 30
        ),
        Situation(
            id = "medium_2",
            title = "Gestión de Batería en Días Nublados",
            description = "Día nublado con 30% de generación normal. Apagón de 8h confirmado de 14h a 22h. Batería: 60% (6kWh). Generarás ~600Wh entre 14h-18h. Tu consumo esencial es 300W. ¿Estrategia óptima?",
            difficulty = SituationDifficulty.MEDIUM,
            options = listOf(
                SituationOption(
                    text = "Cargar batería al máximo antes de las 14h, luego 300W continuo durante apagón",
                    isCorrect = true,
                    explanation = "Correcto. Presupuesto: 6kWh inicial + 0.6kWh generados = 6.6kWh. Consumo: 300W × 8h = 2.4kWh. Sobran 4.2kWh de margen seguro."
                ),
                SituationOption(
                    text = "Usar 500W durante el apagón confiando en la generación",
                    isCorrect = false,
                    explanation = "Incorrecto. 500W × 8h = 4kWh necesarios. Solo tienes 6.6kWh disponibles, margen muy ajustado. Si el clima empeora quedas sin energía."
                ),
                SituationOption(
                    text = "Reducir a 150W para ahorrar al máximo",
                    isCorrect = false,
                    explanation = "Muy conservador. Con 6.6kWh disponibles puedes mantener 300W (esencial completo) sin riesgo. 150W compromete confort innecesariamente."
                ),
                SituationOption(
                    text = "Usar AC durante el día y apagar todo en la noche",
                    isCorrect = false,
                    explanation = "Incorrecto. AC consumiría toda la batería en 4h. Quedarías sin energía crítica para la noche (iluminación, refrigerador)."
                )
            ),
            points = 30
        ),
        Situation(
            id = "medium_3",
            title = "Sobrecarga del Sistema",
            description = "Inversor de 3000W, baterías al 100%. Quieres encender: Refrigerador (150W) + AC (1500W) + Lavadora (500W) + Microondas (800W) + Computadora (300W). ¿Qué sucede?",
            difficulty = SituationDifficulty.MEDIUM,
            options = listOf(
                SituationOption(
                    text = "El inversor se sobrecarga (3250W > 3000W) y se apaga por protección",
                    isCorrect = true,
                    explanation = "Correcto. Consumo total: 3250W excede los 3000W del inversor. Se activará la protección contra sobrecarga y se apagará el sistema."
                ),
                SituationOption(
                    text = "Todo funciona normal porque hay batería suficiente",
                    isCorrect = false,
                    explanation = "Incorrecto. No importa la carga de batería. El inversor tiene límite de potencia instantánea (3000W). 3250W lo sobrecarga y apaga."
                ),
                SituationOption(
                    text = "Funcionará pero las baterías se descargarán muy rápido",
                    isCorrect = false,
                    explanation = "Incorrecto. Nunca funcionará porque el inversor no puede entregar 3250W cuando su máximo es 3000W. Se apagará antes de descargar baterías."
                ),
                SituationOption(
                    text = "Solo se apagarán los equipos de menor prioridad automáticamente",
                    isCorrect = false,
                    explanation = "Incorrecto. Los inversores no priorizan cargas automáticamente. Al sobrepasar 3000W, el sistema completo se apaga por protección."
                )
            ),
            points = 30
        ),
        Situation(
            id = "medium_4",
            title = "Degradación de Paneles",
            description = "Tus paneles de 5kW tienen 7 años. Degradación típica: 0.5%/año. Hoy generaron 3800W a pleno sol (debería ser ~4250W a 85% eficiencia horaria). ¿Diagnóstico?",
            difficulty = SituationDifficulty.MEDIUM,
            options = listOf(
                SituationOption(
                    text = "Hay un problema adicional. Deberían generar ~4082W (5kW × 0.965 × 0.85), no 3800W",
                    isCorrect = true,
                    explanation = "Correcto. Capacidad con degradación: 5kW × (1 - 0.5% × 7 años) = 4.825kW. A 85% eficiencia: 4101W esperados. Los 3800W indican suciedad o fallo."
                ),
                SituationOption(
                    text = "Es normal, la degradación explica la diferencia",
                    isCorrect = false,
                    explanation = "Incorrecto. Con 0.5%/año × 7 años = 3.5% degradación, deberías generar 4101W a pleno sol, no 3800W. Hay 300W de pérdida adicional."
                ),
                SituationOption(
                    text = "Generan más de lo esperado, el sistema está bien",
                    isCorrect = false,
                    explanation = "Incorrecto. Con degradación del 3.5% (7 años), deberías tener 4.825kW × 0.85 = 4101W a pleno sol. 3800W es bajo."
                ),
                SituationOption(
                    text = "Los paneles están muertos, necesitas reemplazarlos",
                    isCorrect = false,
                    explanation = "Exagerado. Generan 3800W de 4101W esperados (93% de lo normal). Probablemente solo necesitan limpieza o revisar conexiones."
                )
            ),
            points = 30
        ),
        Situation(
            id = "medium_5",
            title = "Inversores: On-grid vs Off-grid",
            description = "Frecuentes apagones de 6-8 horas. ¿Qué tipo de inversor es óptimo para tu situación?",
            difficulty = SituationDifficulty.MEDIUM,
            options = listOf(
                SituationOption(
                    text = "Inversor híbrido con almacenamiento en batería",
                    isCorrect = true,
                    explanation = "Correcto. El híbrido permite usar solar directamente, almacenar excedentes, y funcionar durante apagones. Combina ventajas de on-grid y off-grid."
                ),
                SituationOption(
                    text = "Inversor on-grid simple (sin baterías)",
                    isCorrect = false,
                    explanation = "Incorrecto. On-grid se apaga durante cortes de red por seguridad. Quedarías sin electricidad en cada apagón, aunque haya sol."
                ),
                SituationOption(
                    text = "Inversor off-grid puro",
                    isCorrect = false,
                    explanation = "No óptimo. Off-grid funciona en apagones pero no permite vender excedentes ni usar la red cuando la batería está baja. Híbrido es mejor."
                ),
                SituationOption(
                    text = "No necesitas inversor, solo paneles directos",
                    isCorrect = false,
                    explanation = "Incorrecto. Los paneles generan corriente continua (DC). Necesitas inversor para convertirla a AC que usan tus electrodomésticos."
                )
            ),
            points = 30
        ),
        Situation(
            id = "medium_6",
            title = "Carga Simultánea y Descarga",
            description = "Apagón activo. Paneles generan 2000W, baterías al 40%, tu consumo es 1500W. ¿Qué sucede con las baterías?",
            difficulty = SituationDifficulty.MEDIUM,
            options = listOf(
                SituationOption(
                    text = "Se cargan lentamente con el excedente de 500W",
                    isCorrect = true,
                    explanation = "Correcto. Generación (2000W) > Consumo (1500W) = +500W que va a cargar las baterías. Es carga lenta pero positiva."
                ),
                SituationOption(
                    text = "Se descargan porque están en uso",
                    isCorrect = false,
                    explanation = "Incorrecto. Cuando la generación solar excede el consumo, el excedente carga las baterías. Solo se descargan cuando consumo > generación."
                ),
                SituationOption(
                    text = "Permanecen al 40% sin cambios",
                    isCorrect = false,
                    explanation = "Incorrecto. Hay 500W de excedente solar después de cubrir consumo. Ese excedente carga las baterías, aumentando el porcentaje."
                ),
                SituationOption(
                    text = "Se dañan por carga y descarga simultánea",
                    isCorrect = false,
                    explanation = "Incorrecto. Las baterías están diseñadas para esto. El controlador de carga maneja carga/descarga simultánea sin problemas."
                )
            ),
            points = 30
        )
    )

    private val hardSituations = listOf(
        Situation(
            id = "hard_1",
            title = "Crisis Energética Multi-Día",
            description = "Pronóstico: 3 días nublados (20% generación), apagones diarios de 10h (18h-4h). Batería 15kWh al 100%. Consumo crítico: 250W 24h. Consumo día: +400W (10h-18h). ¿Sobrevives los 3 días?",
            difficulty = SituationDifficulty.HARD,
            options = listOf(
                SituationOption(
                    text = "No sin optimizar. Necesitas reducir consumo diurno a 200W o menos",
                    isCorrect = true,
                    explanation = "Correcto. Análisis: Día 1 consume 250W×24h + 400W×8h = 9.2kWh, generas ~4kWh. Déficit diario: 5.2kWh. Batería: 15→9.8→4.6→-0.6kWh. Fallas día 3."
                ),
                SituationOption(
                    text = "Sí, la batería de 15kWh es suficiente para 3 días",
                    isCorrect = false,
                    explanation = "Incorrecto. Consumo diario: 9.2kWh. Generación: ~4kWh. Déficit: 5.2kWh/día × 3 días = 15.6kWh. Excede tu capacidad de 15kWh + 12kWh generados."
                ),
                SituationOption(
                    text = "Sí, solo necesito apagar todo durante las noches",
                    isCorrect = false,
                    explanation = "Incorrecto. Incluso sin consumo nocturno: 250W×24h + 400W×8h = 9.2kWh/día. Con solo 4kWh generados, déficit de 5.2kWh/día agota 15kWh en 3 días."
                ),
                SituationOption(
                    text = "Necesito un generador diésel, solar no funciona en nublado",
                    isCorrect = false,
                    explanation = "Muy pesimista. Solar genera 20% = 4kWh/día. Optimizando a 200W diurno (consumo 7.2kWh/día), déficit: 3.2kWh/día. 15kWh dura los 3 días ajustado."
                )
            ),
            points = 50
        ),
        Situation(
            id = "hard_2",
            title = "Emergencia Médica + Apagón",
            description = "Familiar con CPAP (65W continuo) + concentrador O2 (120W continuo) = 185W/24h críticos. Apagón 14h (20h-10h). Batería 8kWh al 70%. Otros esenciales: 150W. Paneles 3kW. ¿Estrategia de 24h?",
            difficulty = SituationDifficulty.HARD,
            options = listOf(
                SituationOption(
                    text = "Médico 24h (185W), esenciales solo con solar (6h-18h, ~200W). Total: ~6.8kWh, factible con 5.6kWh batería + 8kWh solar generados",
                    isCorrect = true,
                    explanation = "Correcto. Equipos médicos: 185W × 24h = 4.44kWh (crítico). Esenciales solo 12h diurnas: 150W × 12h = 1.8kWh. Total 6.24kWh. Solar genera ~8kWh. Sobra margen."
                ),
                SituationOption(
                    text = "Mantener todo encendido 24h (185W + 150W) = 8kWh, alcanza justo",
                    isCorrect = false,
                    explanation = "Arriesgado. 335W × 24h = 8.04kWh. Con generación solar, sí alcanza, pero sin margen de seguridad. Si hay nubes o error pierdes equipos críticos."
                ),
                SituationOption(
                    text = "Apagar equipos médicos 6 horas durante el día para ahorrar",
                    isCorrect = false,
                    explanation = "¡PELIGROSO! Nunca apagues equipos médicos vitales. La vida del familiar depende de ellos 24h. Apaga esenciales, no equipos críticos de salud."
                ),
                SituationOption(
                    text = "Llamar ambulancia porque no hay suficiente energía",
                    isCorrect = false,
                    explanation = "Innecesario. Tu sistema puede mantener 185W médicos + 150W esenciales con la generación solar diurna. Es manejable sin evacuación."
                )
            ),
            points = 50
        ),
        Situation(
            id = "hard_3",
            title = "Optimización Económica Avanzada",
            description = "Red cobra $0.15/kWh pico (9h-21h), $0.08/kWh valle. Paga $0.10/kWh por excedente solar. Tienes 6kW solar, 10kWh batería. Consumo: 8kWh/día (60% en pico). ¿Estrategia óptima de ahorro?",
            difficulty = SituationDifficulty.HARD,
            options = listOf(
                SituationOption(
                    text = "Usar solar para pico, almacenar excedente, vender solo lo que no cabe en batería, comprar valle si es necesario",
                    isCorrect = true,
                    explanation = "Correcto. Prioridad: 1) Autoconsumo en pico ($0.15 ahorrado) 2) Batería para usar en pico 3) Vender exceso ($0.10) 4) Comprar valle barato ($0.08). Maximiza arbitraje."
                ),
                SituationOption(
                    text = "Vender todo el solar a la red y comprar solo en horario valle",
                    isCorrect = false,
                    explanation = "Incorrecto. Vendes a $0.10 pero compras pico a $0.15. Pierdes $0.05/kWh. Es mejor autoconsumir el solar durante horas pico directamente."
                ),
                SituationOption(
                    text = "Desconectarse de la red completamente y usar solo solar",
                    isCorrect = false,
                    explanation = "Subóptimo. Pierdes beneficio de vender excedentes ($0.10/kWh) y el respaldo de red barata en valle ($0.08/kWh). Mantén la conexión para arbitraje."
                ),
                SituationOption(
                    text = "Cargar baterías de la red en valle y usar esa energía en pico",
                    isCorrect = false,
                    explanation = "Pierdes dinero. Compras a $0.08 y usas en pico (ahorro $0.15) = +$0.07, pero tu solar ya da ese ahorro gratis + vendes exceso. Solar primero siempre."
                )
            ),
            points = 50
        ),
        Situation(
            id = "hard_4",
            title = "Fallo de Inversor Durante Crisis",
            description = "Apagón de 12h, inversor principal (3kW) falla. Tienes inversor backup de onda modificada (1kW). Cargas: Refrigerador (150W), Router (25W), Laptop (60W), CPAP médico (65W), Luces LED (40W), Ventilador (75W). ¿Qué hacer?",
            difficulty = SituationDifficulty.HARD,
            options = listOf(
                SituationOption(
                    text = "Conectar solo CPAP + Refrigerador + Router + 1 luz = 280W. Rotar ventilador y laptop en bloques de 2h",
                    isCorrect = true,
                    explanation = "Correcto. Críticos: CPAP (salud) + Refrigerador (comida) + Router (comunicación) + luz = 280W < 1kW. Ventilador/laptop usarlos alternados según necesidad en las 12h."
                ),
                SituationOption(
                    text = "Conectar todo porque suma 415W, está bajo 1kW",
                    isCorrect = false,
                    explanation = "Arriesgado. Onda modificada tiene picos de arranque. Refrigerador y ventilador tienen motores que pican. Podrías sobrecargar los 1kW. Mejor dejar margen."
                ),
                SituationOption(
                    text = "Solo el CPAP médico, todo lo demás apagado",
                    isCorrect = false,
                    explanation = "Muy conservador. Inversor de 1kW puede manejar 200-300W seguros. Puedes mantener CPAP + refrigerador + comunicación sin comprometer equipo médico."
                ),
                SituationOption(
                    text = "No usar el inversor de onda modificada, esperar el técnico",
                    isCorrect = false,
                    explanation = "Peligroso. Si el técnico tarda, pierdes comida (refrigerador) y pones en riesgo equipo médico (CPAP). Onda modificada es segura para estos equipos a corto plazo."
                )
            ),
            points = 50
        ),
        Situation(
            id = "hard_5",
            title = "Incendio por Cortocircuito Solar",
            description = "Humo sale del panel de conexiones solares. Sospechas cortocircuito. ¿Secuencia correcta de apagado de emergencia?",
            difficulty = SituationDifficulty.HARD,
            options = listOf(
                SituationOption(
                    text = "1) Desconectar DC desde paneles 2) Apagar inversor 3) Desconectar baterías 4) Llamar bomberos si continúa 5) NO usar agua",
                    isCorrect = true,
                    explanation = "Correcto. Protocolo: Cortar fuente DC (paneles) primero, luego inversor, luego baterías. Nunca agua en fuegos eléctricos (extintor clase C). Bomberos si no se controla."
                ),
                SituationOption(
                    text = "Apagar el breaker de la casa y llamar bomberos inmediatamente",
                    isCorrect = false,
                    explanation = "Incorrecto. El breaker AC no detiene DC de paneles/baterías. Debes desconectar DC primero desde los paneles, luego baterías. Entonces llamas bomberos si continúa."
                ),
                SituationOption(
                    text = "Rociar agua sobre los paneles y conexiones para apagar el fuego",
                    isCorrect = false,
                    explanation = "¡MUY PELIGROSO! El agua conduce electricidad DC. Podrías electrocutarte o empeorar el cortocircuito. Nunca agua en fuegos eléctricos. Extintor clase C."
                ),
                SituationOption(
                    text = "Desconectar las baterías primero, luego el inversor",
                    isCorrect = false,
                    explanation = "Incorrecto. Los paneles siguen generando DC aunque desconectes baterías. Debes cortar desde los paneles primero, luego inversor, luego baterías."
                )
            ),
            points = 50
        ),
        Situation(
            id = "hard_6",
            title = "Análisis de Consumo Fantasma",
            description = "Consumo base nocturno: 180W constante. Apagaste todo conscientemente pero la medición sigue. ¿Qué está consumiendo?",
            difficulty = SituationDifficulty.HARD,
            options = listOf(
                SituationOption(
                    text = "Cargas vampiro: Modo standby de TV/equipos (20-40W), Fuentes de poder (10-20W), Calentador water heater en modo (80-100W), Relojes/alarmas (10W)",
                    isCorrect = true,
                    explanation = "Correcto. 180W de 'fantasma' típico: Water heater en standby (80W), TVs/decos apagados (30W), cargadores enchufados (20W), microondas/reloj (15W), otros standby (35W)."
                ),
                SituationOption(
                    text = "Hay un cortocircuito en las paredes",
                    isCorrect = false,
                    explanation = "Muy improbable. 180W constante es típico de cargas standby, no cortocircuito. Cortocircuitos causan picos erráticos o disparan breakers, no consumo constante."
                ),
                SituationOption(
                    text = "El medidor está dañado, no puede ser 180W con todo apagado",
                    isCorrect = false,
                    explanation = "Incorrecto. 180W es consumo fantasma normal en hogar moderno. Standby de electrodomésticos puede sumar 150-250W fácilmente sin que lo notes."
                ),
                SituationOption(
                    text = "Las baterías están en modo de auto-descarga",
                    isCorrect = false,
                    explanation = "Incorrecto. Auto-descarga de baterías es 1-5% mensual, no 180W instantáneos. Ese consumo viene de cargas AC conectadas en standby, no de las baterías."
                )
            ),
            points = 50
        ),
        Situation(
            id = "hard_7",
            title = "Huracán Categoría 3 Aproximándose",
            description = "Huracán en 6 horas. Pronóstico: 3 días sin red, vientos 180km/h. Tienes 5kW paneles en techo, 20kWh baterías al 85%. ¿Preparación crítica?",
            difficulty = SituationDifficulty.HARD,
            options = listOf(
                SituationOption(
                    text = "1) Cargar baterías 100% YA 2) Desconectar paneles/asegurarlos (se pueden volar) 3) Llenar agua/comida 4) Modo isla manual inversor 5) Reducir consumo a 200W",
                    isCorrect = true,
                    explanation = "Correcto. Prioridad: Carga máxima ahora, desconecta paneles (viento los daña), prepara para 3 días sin solar. 20kWh a 200W da 100h (4 días) autonomía. Vivirás con lo almacenado."
                ),
                SituationOption(
                    text = "No hacer nada, los paneles están asegurados y seguirán generando",
                    isCorrect = false,
                    explanation = "PELIGROSO. Vientos 180km/h arrancan paneles mal asegurados. Días nublados = 0-20% solar. Desconecta paneles antes del huracán, usa solo batería."
                ),
                SituationOption(
                    text = "Vender el exceso solar a la red ahora para ganar dinero extra",
                    isCorrect = false,
                    explanation = "Absurdo. En 6h viene un huracán de 3 días. Necesitas cada kWh almacenado. Olvida el dinero, carga baterías al 100% para sobrevivir sin red."
                ),
                SituationOption(
                    text = "Evacuar porque sin red eléctrica es imposible sobrevivir",
                    isCorrect = false,
                    explanation = "Innecesario si tienes 20kWh. Con 200W de consumo esencial tienes 100h (4+ días). Solo evacua si hay riesgo físico (zona inundable), no por electricidad."
                )
            ),
            points = 50
        ),
        Situation(
            id = "hard_8",
            title = "Arbitraje Solar vs Generador Diésel",
            description = "Apagón de 16h. Opciones: 1) Solar (2kW) + batería (8kWh al 60%), 2) Generador diésel 5kW ($2.5/h gasolina). Consumo necesario: 350W continuo. ¿Cuál es más económico y por qué?",
            difficulty = SituationDifficulty.HARD,
            options = listOf(
                SituationOption(
                    text = "Solar+batería es gratis (energía ya almacenada). Generador cuesta $40 (16h × $2.5). Solar ahorra $40 y es silencioso/limpio",
                    isCorrect = true,
                    explanation = "Correcto. Solar: 350W × 16h = 5.6kWh. Tienes 4.8kWh (60%) + generarás ~6kWh diurno = 10.8kWh disponible gratis. Generador: $40 + ruido + CO2. Solar gana."
                ),
                SituationOption(
                    text = "Generador es mejor porque es más potente (5kW vs 2kW)",
                    isCorrect = false,
                    explanation = "Incorrecto. Solo necesitas 350W. Potencia extra no importa. Solar cubre eso gratis, generador cuesta $40 + ruido + mantenimiento. Solar es superior aquí."
                ),
                SituationOption(
                    text = "Usar generador porque las baterías no aguantan 16h",
                    isCorrect = false,
                    explanation = "Incorrecto. Necesitas 5.6kWh. Tienes 4.8kWh en batería + ~6kWh solar diurno = 10.8kWh total. Más que suficiente. No necesitas generador."
                ),
                SituationOption(
                    text = "Combinar ambos: solar de día, generador de noche",
                    isCorrect = false,
                    explanation = "Innecesario y costoso. Tu batería puede almacenar exceso solar diurno para la noche. Generador solo se justifica si batería fuera insuficiente (<3kWh)."
                )
            ),
            points = 50
        )
    )

    private var usedSituationIds = mutableSetOf<String>()

    fun getRandomSituation(difficulty: SituationDifficulty? = null): Situation? {
        val availableSituations = when (difficulty) {
            SituationDifficulty.EASY -> easySituations
            SituationDifficulty.MEDIUM -> mediumSituations
            SituationDifficulty.HARD -> hardSituations
            null -> easySituations + mediumSituations + hardSituations
        }.filter { it.id !in usedSituationIds }

        return availableSituations.randomOrNull()?.also {
            usedSituationIds.add(it.id)
        }
    }

    fun getRandomSituationWeighted(): Situation? {
        val random = kotlin.random.Random.nextFloat()
        val difficulty = when {
            random < 0.2f -> SituationDifficulty.EASY     // 20% fácil
            random < 0.6f -> SituationDifficulty.MEDIUM   // 40% media
            else -> SituationDifficulty.HARD               // 40% difícil
        }
        return getRandomSituation(difficulty)
    }

    fun resetUsedSituations() {
        usedSituationIds.clear()
    }

    fun getAllSituations(): List<Situation> {
        return easySituations + mediumSituations + hardSituations
    }
}
