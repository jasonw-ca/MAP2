//
//  ViewController.swift
//  INFO6125Project2
//
//  Created by Jianwei Wang on 2022-07-24.
//
// A team work by Jianwei and Sampath for project 2 of INFO6125 course of MAP Program at Fanshawe College

import UIKit
import CoreData

class ViewController: UIViewController {
    
    @IBOutlet weak var incomeText: UILabel!
    @IBOutlet weak var expenseText: UILabel!
    @IBOutlet weak var budgetText: UILabel!
    @IBOutlet weak var savingText: UILabel!
    @IBOutlet weak var budgetProgressView: UIProgressView!
    @IBOutlet weak var dollorImageView: UIImageView!
    private var records:[MoneyRecord] = []
    let defaults = UserDefaults.standard
    private let keyBudget = "Budget"
    private let keyIncome = "Income"
    private let keyExpense = "Expense"
    private var progressViewValue: Float = 0.0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        sfImageConfig()
        
        //Clearing user defaults for testing, comment out the following 4 lines after clearing.
//        let domain = Bundle.main.bundleIdentifier!
//        UserDefaults.standard.removePersistentDomain(forName: domain)
//        UserDefaults.standard.synchronize()
//        print(Array(UserDefaults.standard.dictionaryRepresentation().keys).count)
    }
    
    override func viewDidAppear(_ animated: Bool) {
        
        let budget = defaults.string(forKey: keyBudget) ?? "0"
        budgetText.text = "$"+budget
        let income = defaults.integer(forKey: keyIncome)
        incomeText.text = "$\(income)"
        let expense = defaults.integer(forKey: keyExpense)
        expenseText.text = "$\(expense)"
        savingText.text = "$"+String(income - expense)
        guard let intBudget = Int(budget) else {return}
        progressViewValue = Float(expense)/Float(intBudget)
        budgetProgressView.progress = progressViewValue
    }
    @IBAction func onBudgetButtonTapped(_ sender: UIButton) {
        
        var budgetTextField: UITextField?
        let alertController = UIAlertController(
            title: "Budget",
            message: "Please enter your budget for this month",
            preferredStyle: .alert)
        
        let confirmAction = UIAlertAction(
            title: "Confirm", style: .default) { [self]
                (action) -> Void in
                
                if let budget = budgetTextField?.text {
                    
                    self.defaults.set(budget,forKey: self.keyBudget)
                    
                    print(" Budget = \(budget)")
                    self.budgetText.text = "$ \(budget)"
                    let expense = defaults.integer(forKey: keyExpense)
                    guard let intBudget = Int(budget) else {return}
                    progressViewValue = Float(expense)/Float(intBudget)
                    budgetProgressView.progress = progressViewValue
                } else {
                    print("No Budget Entered")
                }
            }
        let cancel = UIAlertAction(
            title: "Cancel", style: .cancel) {_ in
            }
        alertController.addTextField {
                (txtBudget) -> Void in
                budgetTextField = txtBudget
                budgetTextField!.placeholder = "$CAD"
            }
        alertController.addAction(confirmAction)
        alertController.addAction(cancel)
        present(alertController, animated: true)
    }
    
    
    
    
    private func sfImageConfig(){
        let config = UIImage.SymbolConfiguration(scale: .large)
        dollorImageView.preferredSymbolConfiguration = config
        dollorImageView.image = UIImage(systemName: "banknote")
    }
    
//    private func loadFromDB (){
//        guard let context = getCoreContext() else{
//            return
//        }
//        let request = MoneyRecord.fetchRequest()
//        do{
//            try records = context.fetch((request))
//
//        }catch{
//            print(error)
//        }
//
//    }
//
//    private func getCoreContext()->NSManagedObjectContext?{
//        (UIApplication.shared.delegate as? AppDelegate)?.persistentContainer.viewContext
//    }
    
    @IBAction func historyButtonTapped(_ sender: UIButton) {
        goNextScreen(screen: "goToHistory")
    }
    
    
    @IBAction func addButtonTapped(_ sender: UIButton) {
        goNextScreen(screen: "goToAdd")
    }
    
    @IBAction func mapButtonTapped(_ sender: UIButton) {
        goNextScreen(screen: "goToMap")
    }
    
    private func goNextScreen(screen: String){
        performSegue(withIdentifier: screen, sender: self)
    }
}
