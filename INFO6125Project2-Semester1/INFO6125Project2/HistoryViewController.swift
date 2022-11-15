//
//  HistoryViewController.swift
//  INFO6125Project2
//
//  Created by Sampath Bandara on 2022-08-08.
//

import UIKit
import CoreData

class HistoryViewController: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    let defaults = UserDefaults.standard
    
    private var records: [MoneyRecord] = []
    override func viewDidLoad() {
        super.viewDidLoad()
        //loadDefaultRecord()
    
        loadFromDB()
        tableView.dataSource = self
        tableView.delegate = self
        //tableView.reloadData()
    }
    
//    private func loadDefaultRecord(){
//        records.append(moneyRecord(type: "expense", category: "grocery", value: 0, date:"12-08-2022"))
//    }
    @IBAction func anotherButtonTapped(_ sender: Any) {
        performSegue(withIdentifier: "goToAdd", sender: self)
    }
    
    @IBAction func homeButtonTapped(_ sender: UIButton) {
        performSegue(withIdentifier: "goToHome", sender: self)
        
    }
    private func loadFromDB (){
        guard let context = getCoreContext() else{
            return
        }
        let request = MoneyRecord.fetchRequest()
        do{
            try records = context.fetch((request))
            
        }catch{
            print(error)
        }
        
    }
    
    private func getCoreContext()->NSManagedObjectContext?{
        (UIApplication.shared.delegate as? AppDelegate)?.persistentContainer.viewContext
    }
}



extension HistoryViewController: UITableViewDataSource{
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return records.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "moneyRecordCell", for: indexPath)
        let item = records[indexPath.row]
        var content = cell.defaultContentConfiguration()
        content.text = "$"+(item.value ?? "0")
        content.secondaryText = (item.type ?? "default") + " | " + (item.category ?? "default") + " | " + (item.date ?? "default")
        
        //Get lat and lng from database and print out
        print("\(item.lat),\(item.lng)")
        cell.contentConfiguration = content
        return cell
    }
    
    
}

extension HistoryViewController: UITableViewDelegate{
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        tableView.deselectRow(at: indexPath, animated: true)
        let alertController = UIAlertController(title: "Delete Record", message: "Are you sure you want to delete this record?", preferredStyle: .alert)
        alertController.addAction(UIAlertAction(title: "No", style: .cancel))
        alertController.addAction(UIAlertAction(title: "Yes", style: .default, handler: { action in
            let record = self.records[indexPath.row]
            // Read data from user defaults and subtract the deleted value and save.
            if(record.type=="Expense"){
                let expense = self.defaults.integer(forKey: "Expense")
                //print("Expense", expense)
                self.defaults.set(expense - (Int(record.value ?? "0") ?? 0), forKey: "Expense")
            }
            if(record.type=="Income"){
                let income = self.defaults.integer(forKey: "Income")
                //print("Expense", expense)
                self.defaults.set(income - (Int(record.value ?? "0") ?? 0), forKey: "Income")
            }
            self.getCoreContext()?.delete(self.records[indexPath.row])
            self.records.remove(at: indexPath.row)
            (UIApplication.shared.delegate as? AppDelegate)?.saveContext()
            tableView.reloadData()
        }))
        self.present(alertController, animated: true)
    }
    
    
}
