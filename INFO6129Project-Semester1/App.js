import { useEffect } from "react";
import { StatusBar } from "expo-status-bar";
import { StyleSheet, Text, View, Button } from "react-native";
import HomeScreen from "./components/Homescreen/Home";
import HistoryScreen from "./components/Historyscreen/History";
import SettingsScreen from "./components/Settingsscreen/Settings";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { NavigationContainer } from "@react-navigation/native";
import Ionicons from "react-native-vector-icons/Ionicons";
import * as SplashScreen from "expo-splash-screen";
import * as Notifications from 'expo-notifications';



export default function App() {
  useEffect(() => {
    SplashScreen.preventAutoHideAsync()
      .then((r) => {
        console.log("Prevented? ", r);
      })
      .catch((e) => {
        console.log("Error: ", e);
      });
  }, []);

  

  const Tab = createBottomTabNavigator();
  return (
    <NavigationContainer>
      <Tab.Navigator
        screenOptions={({ route }) => ({
          headerStyle: {
            backgroundColor: "#4287f5",
          },
          headerTintColor: "#fff",
          headerTitleStyle: {
            fontWeight: "bold",
          },
          tabBarIcon: ({ focused, color, size }) => {
            let iconName;

            if (route.name === "Home") {
              iconName = focused ? "home" : "home-outline";
            } else if (route.name === "Settings") {
              iconName = focused
                ? "ios-list-circle"
                : "ios-list-circle-outline";
            } else if (route.name === "History") {
              iconName = focused ? "time" : "time-outline";
            }

            // You can return any component that you like here!
            return <Ionicons name={iconName} size={size} color={color} />;
          },
          tabBarActiveTintColor: "#143ab8",
          tabBarInactiveTintColor: "white",
          tabBarStyle: {
            // position:"absolute",
            backgroundColor: "#4287f5",
          },
        })}
      >
        <Tab.Screen name="Home" component={HomeScreen} />
        <Tab.Screen name="History" component={HistoryScreen} />
        <Tab.Screen name="Settings" component={SettingsScreen} />
      </Tab.Navigator>
    </NavigationContainer>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "white",
    alignItems: "center",
    justifyContent: "center",
  },
});
