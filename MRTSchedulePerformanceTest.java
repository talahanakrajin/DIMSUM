import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MRTSchedulePerformanceTest {
   private static Map<String, List<StationTime>> mrtSchedule = new HashMap();
   private static Scanner scanner;

   public MRTSchedulePerformanceTest() {
   }

   public static void main(String[] var0) {
      int repeatCount = 10;
      // First, test viewing full schedule with 0 trains (should be empty)
      complexityTest(2, 0, repeatCount);
      int var1 = 10;
      for(int var2 = 1; var2 <= 7; ++var2) {
         complexityTest(var2, var1, repeatCount);
      }
   }

   public static void complexityTest(int var0, int var1, int repeatCount) {
      double totalTime = 0;
      double totalMemory = 0;
      for (int i = 0; i < repeatCount; i++) {
         mrtSchedule.clear();
         Runtime var2 = Runtime.getRuntime();
         var2.gc();
         long var3 = var2.totalMemory() - var2.freeMemory();
         long var5 = System.nanoTime();
         label30:
         switch (var0) {
            case 1:
               int var7 = 0;
               while(true) {
                  if (var7 >= var1) {
                     break label30;
                  }
                  String var8 = "Train" + var7;
                  ArrayList var9 = new ArrayList();
                  for(int var10 = 0; var10 < 12; ++var10) {
                     var9.add(new StationTime("Station" + var10, "10:00"));
                  }
                  mrtSchedule.put(var8, var9);
                  ++var7;
               }
            case 2:
               viewFullSchedule();
               break;
            case 3:
               viewStationSchedule("Bundaran HI");
               break;
            case 4:
               findNextArrivingTrain("Bundaran HI", "10:00");
               break;
            case 5:
               rescheduleTrain("Train A", "Bundaran HI", "11:00");
               break;
            case 6:
               delayTrain("Train A", 10);
               break;
            case 7:
               cancelTrain("Train0");
         }
         long var15 = System.nanoTime();
         var2.gc();
         long var16 = var2.totalMemory() - var2.freeMemory();
         double var11 = (double)(var15 - var5) / 1000000.0;
         double var13 = (double)(var16 - var3) / 1024.0;
         totalTime += var11;
         totalMemory += var13;
      }
      double avgTime = totalTime / repeatCount;
      double avgMemory = totalMemory / repeatCount;
      System.out.println("Average Time: " + avgTime + " ms");
      System.out.println("Average Memory: " + avgMemory + " KB\n");
   }

   private static void viewFullSchedule() {
      Iterator var0 = mrtSchedule.entrySet().iterator();

      while(var0.hasNext()) {
         Map.Entry var1 = (Map.Entry)var0.next();

         StationTime var3;
         for(Iterator var2 = ((List)var1.getValue()).iterator(); var2.hasNext(); var3 = (StationTime)var2.next()) {
         }
      }

   }

   private static void viewStationSchedule(String var0) {
      Iterator var1 = mrtSchedule.entrySet().iterator();

      while(var1.hasNext()) {
         Map.Entry var2 = (Map.Entry)var1.next();
         Iterator var3 = ((List)var2.getValue()).iterator();

         while(var3.hasNext()) {
            StationTime var4 = (StationTime)var3.next();
            if (var4.station.equalsIgnoreCase(var0)) {
            }
         }
      }

   }

   private static void findNextArrivingTrain(String var0, String var1) {
      LocalTime var2 = LocalTime.parse(var1, DateTimeFormatter.ofPattern("H:mm"));
      Iterator var3 = mrtSchedule.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry var4 = (Map.Entry)var3.next();
         Iterator var5 = ((List)var4.getValue()).iterator();

         while(var5.hasNext()) {
            StationTime var6 = (StationTime)var5.next();
            if (var6.station.equalsIgnoreCase(var0)) {
               try {
                  LocalTime var7 = LocalTime.parse(var6.time, DateTimeFormatter.ofPattern("H:mm"));
                  if (var7.isAfter(var2)) {
                  }
               } catch (Exception var8) {
               }
            }
         }
      }

   }

   private static void rescheduleTrain(String var0, String var1, String var2) {
      List var3 = (List)mrtSchedule.get(var0);
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            StationTime var5 = (StationTime)var4.next();
            if (var5.station.equalsIgnoreCase(var1)) {
               var5.time = var2;
               break;
            }
         }
      }

   }

   private static void delayTrain(String var0, int var1) {
      List var2 = (List)mrtSchedule.get(var0);
      if (var2 != null) {
         DateTimeFormatter var3 = DateTimeFormatter.ofPattern("H:mm");
         Iterator var4 = var2.iterator();

         while(var4.hasNext()) {
            StationTime var5 = (StationTime)var4.next();

            try {
               LocalTime var6 = LocalTime.parse(var5.time, var3);
               var6 = var6.plusMinutes((long)var1);
               var5.time = var6.format(var3);
            } catch (Exception var7) {
            }
         }
      }

   }

   private static void cancelTrain(String var0) {
      mrtSchedule.remove(var0);
   }

   private static void initializeSchedule() {
      mrtSchedule.put("Train A", new ArrayList(Arrays.asList(new StationTime("Bundaran HI", "5:00"), new StationTime("Dukuh Atas BNI", "5:05"), new StationTime("Setiabudi Astra", "5:10"), new StationTime("Bendungan Hilir", "5:15"), new StationTime("Istora", "5:20"), new StationTime("Senayan", "5:25"), new StationTime("ASEAN", "5:30"), new StationTime("Blok M", "5:35"), new StationTime("Blok A", "5:40"), new StationTime("Haji Nawi", "5:45"), new StationTime("Cipete Raya", "5:50"), new StationTime("Fatmawati", "5:55"))));
   }

   static {
      scanner = new Scanner(System.in);
   }
}
