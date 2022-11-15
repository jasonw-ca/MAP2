import { useState, useEffect } from "react";
import {
  View,
  StyleSheet,
  Text,
  ImageBackground,
  TouchableOpacity,
  Alert,
} from "react-native";
import { Picker } from "@react-native-picker/picker";
import * as SplashScreen from "expo-splash-screen";
import { today } from "../../tools/tools";

import {
  dbGetUser,
  dbInit,
  dropUser,
  initTable,
  dbUpdateWater,
} from "../../tools/sqlite";
import * as ProfileDb from "../../tools/profiledb";

import * as Notifications from "expo-notifications";

export default function Main(props) {
  const [target, setTarget] = useState(0);
  const [waterAmount, setWaterAmount] = useState(0);
  const [selectedValue, setSelectedValue] = useState(100);
  const [targetLabel, setTargetLabel] = useState("TODAY'S WATER TARGET");
  const [heightMultiplier, setHeightMultiplier] = useState(1);
  const [weightMultiplier, setWeightMultiplier] = useState(1);

  useEffect(() => {
    (async () => {
      try {
        //dropUser();
        await ProfileDb.dbInit()
          .then((result) => {
            console.log("dbProfileInit: ", result);

            ProfileDb.dbGetProfile()
              .then((result) => {
                const dbProfile = result.rows._array;
                //console.log("dbProfile: ", dbProfile);
                const newestRow = dbProfile[dbProfile.length - 1];
                //console.log("Newest row: ", newestRow);
                setHeightMultiplier(newestRow.height / 160);
                setWeightMultiplier(newestRow.weight / 50);
                console.log("HM: ", heightMultiplier);
                console.log("WM: ", weightMultiplier);
              })
              .catch((error) => {
                console.log("Get Profile Error: ", error);
              });
          })
          .catch((error) => {
            console.log("Init Error: ", error);
          });
        await dbInit()
          .then((result) => {
            console.log("dbInit: ", result);

            dbGetUser()
              .then((result) => {
                const dbUser = result.rows._array;
                console.log("dbUser: ", dbUser);
                //If database is empty (user opens the app for the 1st time) initiate the table and UI.

                if (dbUser.length == 0) {
                  initTable(1000)
                    .then((result) => {
                      //console.log("Init Result: ", result);

                      dbGetUser()
                        .then((result) => {
                          const dbUser = result.rows._array;
                          console.log("dbUser: ", dbUser);
                          const newestRow = dbUser[dbUser.length - 1];
                          setWaterAmount(newestRow.water);
                          setTarget(newestRow.target);
                        })
                        .catch((error) => {
                          console.log("Get Error: ", error);
                        });
                    })
                    .catch((error) => {
                      console.log("Add Error: ", error);
                    });
                } else {
                  //If a new day begins, reset the table and UI with initTable().

                  const newestRow = dbUser[dbUser.length - 1];
                  //console.log("The date: ", newestRow.date);
                  //Load data from the newest row of database into UI.

                  if (newestRow.date != today()) {
                    initTable(1000)
                      .then((result) => {
                        console.log("New Date Init Result: ", result._array);
                        dbGetUser()
                          .then((result) => {
                            const dbUser = result.rows._array;
                            const newestRow = dbUser[dbUser.length - 1];
                            //console.log("dbUser: ", dbUser);
                            setWaterAmount(newestRow.water);

                            //If user did not enter profile, use default target of 1000ml

                            if (
                              heightMultiplier == 0 ||
                              weightMultiplier == 0
                            ) {
                              setTarget(newestRow.target);
                            } else {
                              setTarget(
                                newestRow.target *
                                  heightMultiplier *
                                  weightMultiplier
                              );
                            }
                          })
                          .catch((error) => {
                            console.log("Get Error: ", error);
                          });
                      })
                      .catch((error) => {
                        console.log("Add Error: ", error);
                      });
                  } else {
                    setWaterAmount(newestRow.water);
                    if (heightMultiplier == 0 || weightMultiplier == 0) {
                      setTarget(newestRow.target);
                    } else {
                      setTarget(
                        newestRow.target * heightMultiplier * weightMultiplier
                      );
                    }
                  }
                }

                SplashScreen.hideAsync().catch((error) => {
                  console.log("SS Error: ", error);
                });
              })
              .catch((error) => {
                console.log("Get Error: ", error);
              });
          })
          .catch((error) => {
            console.log("Init Error: ", error);
          });
      } catch (e) {
        console.log("error: ", e);
      }
    })();
  }, [heightMultiplier, weightMultiplier]);

  useEffect(() => {
    Notifications.scheduleNotificationAsync({
      content: {
        title: "Time for water",
        body: "Water makes your healthy",
      },
      trigger: {
        seconds: 10,
        repeats: true,
      },
    })
      .then()
      .catch((error) => {
        console.log("Get Error: ", error);
      });

    Notifications.setNotificationHandler({
      handleNotification: async () => ({
        shouldShowAlert: true,
        shouldPlaySound: true,
        shouldSetBadge: false,
      }),
    });
  }, []);

  const addWater = () => {
    Alert.alert("You drank some water", "Is the volume correct?", [
      {
        text: "Cancel",
        onPress: () => {
          console.log("Cancel pressed");
        },
        style: "cancel",
      },
      {
        text: "Confirm",
        onPress: () => {
          setWaterAmount(waterAmount + selectedValue);
          dbUpdateWater(waterAmount + selectedValue, today())
            .then((result) => {
              console.log("Update Result: ", result);
            })
            .catch((error) => {
              console.log("Update Error: ", error);
            });
          //Check if the target is completed, and then do the following
          if (waterAmount / target >= 1) {
            //Cancel notification
            Notifications.getAllScheduledNotificationsAsync().then(
              (notifications) => {
                notifications.forEach((notification) => {
                  Notifications.cancelScheduledNotificationAsync(
                    notification.identifier
                  );
                });
              }
            );
            setTargetLabel("Good Job! Target Done");
          }
        },
      },
    ]);
  };

  return (
    <View style={styles.container}>
      <View style={styles.targetContainer}>
        <Text style={styles.targetText}>{targetLabel}</Text>
        <Text style={styles.targetText}>{parseInt(target)}ML</Text>
      </View>
      <View style={styles.amountContainer}>
        <ImageBackground
          source={require("../../assets/waterDrop.jpg")}
          resizeMode="contain"
          style={styles.imageBackground}
        >
          <Text style={styles.amountText}>{waterAmount}ML</Text>
          <Text style={styles.amountText}>
            {parseInt((waterAmount / target) * 100)}%
          </Text>
        </ImageBackground>
      </View>
      <View style={styles.inputContainer}>
        <View style={styles.pickerContainer}>
          <Picker
            selectedValue={selectedValue}
            onValueChange={(v, i) => {
              setSelectedValue(v);
            }}
          >
            <Picker.Item label="100ml" value={100} />
            <Picker.Item label="200ml" value={200} />
            <Picker.Item label="300ml" value={300} />
            <Picker.Item label="400ml" value={400} />
            <Picker.Item label="500ml" value={500} />
          </Picker>
        </View>

        <TouchableOpacity
          activeOpacity={0.7}
          style={styles.button}
          onPress={addWater}
        >
          <Text style={{ fontSize: 16, fontWeight: "bold", color: "white" }}>
            ADD
          </Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 7,
    flexDirection: "column",
    width: "100%",

    alignItems: "center",
    justifyContent: "space-evenly",
    paddingHorizontal: 0,
  },
  targetContainer: {
    flex: 2,
    alignItems: "center",
    justifyContent: "center",
  },
  targetText: {
    fontSize: 20,
    color: "#143ab8",
    fontWeight: "600",
  },
  amountContainer: {
    flex: 4,
    padding: 0,
    width: "100%",

    justifyContent: "center",
  },
  imageBackground: {
    flex: 1,
    paddingTop: 120,
    alignItems: "center",
    // justifyContent: "center",
  },
  amountText: {
    color: "white",
    fontSize: 30,
  },
  inputContainer: {
    flex: 2,
    alignItems: "center",
    justifyContent: "space-evenly",
  },
  pickerContainer: {
    height: 40,
    width: 150,
    borderColor: "#143ab8",
    borderRadius: 5,
    borderWidth: 1,
    justifyContent: "center",
  },
  button: {
    height: 30,
    width: 150,
    backgroundColor: "#143ab8",
    alignItems: "center",
    justifyContent: "center",
    borderRadius: 10,
  },
});
