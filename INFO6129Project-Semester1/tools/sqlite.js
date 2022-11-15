import * as SQLite from "expo-sqlite";
import { thisMonth, today } from "./tools";

const database = SQLite.openDatabase("water.db");

export function dbInit() {
  return new Promise((resolve, reject) => {
    database.transaction(
      (tx) => {
        tx.executeSql(
          `CREATE TABLE IF NOT EXISTS User (
                  id INTEGER PRIMARY KEY NOT NULL,
                  date TEXT NOT NULL,
                  target INTEGER, 
                  water INTEGER ,
                  month INTEGER
                )
                `,
          [],
          (tx, rsltSet) => {
            resolve(rsltSet);
          },
          (tx, err) => {
            reject(err);
          }
        );
      },
      (err) => {
        reject(err);
      },
      () => {
        console.log("Tx success.");
      }
    );
  });
}

export function dbGetUser() {
  return new Promise((resolve, reject) => {
    database.transaction(
      (tx) => {
        tx.executeSql(
          `SELECT id, date, target, water, month FROM User`,
          [],
          (tx, rsltSet) => {
            resolve(rsltSet);
          },
          (tx, err) => {
            reject(err);
          }
        );
      },
      (err) => {
        reject(err);
      },
      () => {
        console.log("Add Tx success.");
      }
    );
  });
}

export function dbGetMonthly() {
  return new Promise((resolve, reject) => {
    database.transaction(
      (tx) => {
        tx.executeSql(
          `SELECT water, month FROM User
          WHERE month=?
          `,
          [thisMonth()],
          (tx, rsltSet) => {
            resolve(rsltSet);
          },
          (tx, err) => {
            reject(err);
          }
        );
      },
      (err) => {
        reject(err);
      },
      () => {
        console.log("Add Tx success.");
      }
    );
  });
}

export function dbMonthGroup() {
  return new Promise((resolve, reject) => {
    database.transaction(
      (tx) => {
        tx.executeSql(
          `SELECT month,
            AVG(water) as avgWater
          FROM User
          GROUP BY month
          `,
          [],
          (tx, rsltSet) => {
            resolve(rsltSet);
          },
          (tx, err) => {
            reject(err);
          }
        );
      },
      (err) => {
        reject(err);
      },
      () => {
        console.log("Add Tx success.");
      }
    );
  });
}

export function initTable(target) {
  return new Promise((resolve, reject) => {
    database.transaction(
      (tx) => {
        tx.executeSql(
          `INSERT INTO User (date, target, water, month)
             VALUES(?,?,?,?)
            `,
          [today(), target, 0, thisMonth()],
          (tx, rsltSet) => {
            resolve(rsltSet);
          },
          (tx, err) => {
            reject(err);
          }
        );
      },
      (err) => {
        reject(err);
      },
      () => {
        console.log("Tx success.");
      }
    );
  });
}

// export function dbAddWater() {
//   return new Promise((resolve, reject) => {
//     database.transaction(
//       (tx) => {
//         tx.executeSql(
//           `INSERT INTO User (date, target, water, month)
//              VALUES(?,?,?,?)
//             `,
//           [20220804, 1, 1, 1],
//           (tx, rsltSet) => {
//             resolve(rsltSet);
//           },
//           (tx, err) => {
//             reject(err);
//           }
//         );
//       },
//       (err) => {
//         reject(err);
//       },
//       () => {
//         console.log("Tx success.");
//       }
//     );
//   });
// }

export function dbUpdateWater(currentWater, date) {
  return new Promise((resolve, reject) => {
    database.transaction(
      (tx) => {
        tx.executeSql(
          `UPDATE User
             SET water = ?
             WHERE date = ?
            `,
          [currentWater, date],
          (tx, rsltSet) => {
            resolve(rsltSet);
          },
          (tx, err) => {
            reject(err);
          }
        );
      },
      (err) => {
        reject(err);
      },
      () => {
        console.log("Update Tx success.");
      }
    );
  });
}

export function dropUser() {
  database.transaction((tx) => {
    tx.executeSql(
      `Drop TABLE IF EXISTS User
            `,
      [],
      () => console.log("Table deleted"),
      (tx, err) => console.log("Failed to delete table: ", err)
    );
  });
}
