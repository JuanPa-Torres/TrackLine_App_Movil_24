package com.example.trackline24;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TrainingPlan {
    public static List<String> generarPlanEntrenamiento(double distanciaTotal, Date fechaInicio, Date fechaFin, String nivel) {
        Integer nivelN;
        if(nivel.equals("Principiante")){
            nivelN = 1;
        } else if (nivel.equals("Medio")){
            nivelN = 2;
        } else {
            nivelN = 3;
        }

        List<String> plan = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Calcular el total de días
        Calendar cal = Calendar.getInstance();
        cal.setTime(fechaInicio);
        long diferenciaMilis = fechaFin.getTime() - fechaInicio.getTime();
        int diasTotales = (int) (diferenciaMilis / (1000 * 60 * 60 * 24)) + 1;

        // Ajustar días de descanso según el nivel
        int diasDescanso;
        if (nivelN == 1) { // Principiante
            diasDescanso = diasTotales / 4; // Descanso cada 4 días
        } else if (nivelN == 2) { // Intermedio
            diasDescanso = diasTotales / 6; // Descanso cada 6 días
        } else { // Avanzado
            diasDescanso = diasTotales / 8; // Descanso cada 8 días
        }
        int diasEntrenamiento = diasTotales - diasDescanso;

        // Calcular incremento diario
        double incrementoDiario = distanciaTotal / diasEntrenamiento;
        int contadorEntrenamiento = 0; // Contador para controlar días de entrenamiento

        for (int i = 0; i < diasTotales; i++) {
            if (contadorEntrenamiento == (nivelN == 1 ? 3 : (nivelN == 2 ? 5 : 7))) { // Ajustar descanso por nivel
                plan.add(sdf.format(cal.getTime()) + ": Descanso");
                contadorEntrenamiento = 0;
            } else {
                double distanciaDia = Math.min((i + 1) * incrementoDiario, distanciaTotal);
                plan.add(sdf.format(cal.getTime()) + ": Actividad " + String.format("%.2f", distanciaDia) + " km");
                contadorEntrenamiento++;
            }
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return plan;
    }

    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaInicio = sdf.parse("02/12/2024");
        Date fechaFin = sdf.parse("22/12/2024");
        double objetivoKm = 10.0;
        String nivel = "Principiante"; // Nivel 1 = Principiante

        List<String> planEntrenamiento = generarPlanEntrenamiento(objetivoKm, fechaInicio, fechaFin, nivel);
        for (String dia : planEntrenamiento) {
            System.out.println(dia);
        }
    }
}
