//
//  AddViewController.swift
//  INFO6125Project2
//
//  Created by Sampath Bandara on 2022-08-08.
//

import UIKit
import CoreLocation
import CoreData

class AddViewController: UIViewController {
    
    @IBOutlet weak var figureInput: UITextField!
    
    @IBOutlet weak var datePicker: UIDatePicker!
    @IBOutlet weak var expenseButton: UIButton!
    @IBOutlet weak var incomeButton: UIButton!
    @IBOutlet weak var moneyTypePicker: UIPickerView!
    
    @IBOutlet weak var moneyTypeLabel: UILabel!
    
    
    private let locationManager = CLLocationManager()
    private let pickerData = ["Grocery", "Transportation", "Education", "Entertainment", "Garments", "Health"]
    
    private var moneyType = ""
    private var expenseCategory = ""
    private var records : [MoneyRecord] = []
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    let defaults = UserDefaults.standard
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Data for moneyTypePicker and connect them
        self.moneyTypePicker.delegate = self
        self.moneyTypePicker.dataSource = self
        
        // related to get the current location
        locationManager.delegate = self
        
        locationManager.requestWhenInUseAuthorization()
        //locationManager.requestLocation()
        locationManager.startUpdatingLocation()
    }
    
    private func displayLocation(location: String) {
        //print(location)
    }
    
    @IBAction func incomeButtonTapped(_ sender: Any) {
        //Once selected, disable and enable Expense button
        incomeButton.isEnabled = false
        expenseButton.isEnabled = true
        moneyTypeLabel.text = "This is an income"
        moneyType = "Income"
        moneyTypePicker.isUserInteractionEnabled = false
        
        
    }
    @IBAction func expenseButtonTapped(_ sender: Any) {
        
        incomeButton.isEnabled = true
        expenseButton.isEnabled = false
        moneyTypeLabel.text = "This is an expense"
        moneyType="Expense"
        moneyTypePicker.isUserInteractionEnabled = true
    }
    
    @IBAction func saveButtonTapped(_ sender: Any) {
        let moneyValue = figureInput.text
        
        //Check user's input
        if (moneyType=="" || moneyValue==nil){
            let alertController = UIAlertController(title: "Something missing", message: "Did you select a money type and enter the value?", preferredStyle: .alert)
            alertController.addAction(UIAlertAction(title: "OK", style: .default))
            present(alertController, animated: true)
        } else{
            let formattedDate = datePicker.date.formatted(
                .dateTime
                    .day().month().year()
                
            )
            // Getting total income or expense from user defauts and update it
            if(moneyType=="Expense"){
                let expense = defaults.integer(forKey: "Expense")
                //print("Expense", expense)
                defaults.set(expense + (Int(moneyValue ?? "0") ?? 0), forKey: "Expense")
            }
            if(moneyType=="Income"){
                let expense = defaults.integer(forKey: "Income")
                //print("Expense", expense)
                defaults.set(expense + (Int(moneyValue ?? "0") ?? 0), forKey: "Income")
            }
            
            
            guard let context = getCoreContext() else {
                return
            }
            let record = MoneyRecord(context: context)
            //print(moneyType, moneyValue ?? "0", expenseCategory,formattedDate)
            record.type = moneyType
            
            record.category = (moneyType=="Income") ? "General" : expenseCategory
            record.value = moneyValue
            record.date = formattedDate
            record.lat = latitude
            record.lng = longitude
            records.append(record)
            
            // Save data to DB
            (UIApplication.shared.delegate as? AppDelegate)?.saveContext()
            //print(records[0])
            
            
            performSegue(withIdentifier: "goToHistory", sender: self)
        }
        
        
        
        
    }
    
    @IBAction func cancelButtonTapped(_ sender: UIButton) {
        dismiss(animated: true, completion:nil)
    }
    private func getCoreContext()->NSManagedObjectContext?{
        (UIApplication.shared.delegate as? AppDelegate)?.persistentContainer.viewContext
    }
    
}

// related to get the current location
extension AddViewController: CLLocationManagerDelegate {
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        if let location = locations.last {
            latitude = location.coordinate.latitude
            longitude = location.coordinate.longitude
            
            //displayLocation(location: "(\(latitude),\(longitude))")
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Error happened with location tracking")
    }
}

extension AddViewController: UIPickerViewDelegate, UIPickerViewDataSource{
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        return pickerData.count
    }
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        //print(pickerData[row])
        expenseCategory = pickerData[row]
        return pickerData[row]
    }
}
