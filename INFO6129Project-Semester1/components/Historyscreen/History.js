import React, { useState, useEffect } from "react";
import {
  View,
  StyleSheet,
  Text,
  Button,
  Snackbar,
  ScrollView,
} from "react-native";
import { dbMonthGroup, dbGetMonthly, dbGetUser } from "../../tools/sqlite";
import HistoryItem from "./HistoryItem";

export default function () {
  const [monthlyRecord, setMonthlyRecord] = useState([]);

  const handleRefresh = () => {
    dbMonthGroup()
      .then((r) => {
        //console.log("MonthGroup result: ", r.rows._array);
        setMonthlyRecord(r.rows._array);
        //console.log("Monthly Records: ", monthlyRecord[1].month);
      })
      .catch((error) => {
        console.log("MonthGroup Error: ", error);
      });
  };
   useEffect(() => {
    handleRefresh();
   }, []);

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={{ fontSize: 20, fontWeight: "bold" }}>
          MONTHLY AVERAGE
        </Text>
        <Button title="REFRESH" onPress={handleRefresh} />
      </View>
      <View style={styles.list}>
        <ScrollView>
          {monthlyRecord.map((record, index) => {
            return (
              <View key={index}>
                <HistoryItem
                  month={record.month}
                  amount={record.avgWater.toFixed(1)}
                />
              </View>
            );
          })}
        </ScrollView>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,

    flexDirection: "column",
    backgroundColor: "white",
    alignItems: "center",
    justifyContent: "center",
  },
  header: {
    flex: 1,
    width: "100%",
    flexDirection:"row",

    alignItems: "center",
    justifyContent: "space-around",
    paddingHorizontal: 20,
    borderBottomColor: "#4287f5",
    borderBottomWidth: 3,
  },
  list: {
    flex: 9,
    width: "100%",
  },
});
