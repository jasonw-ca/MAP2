//
//  ViewController.swift
//  INFO6125 Project1
//
//  Created by Jason Wang on 2022-06-17.
//

import UIKit

class ViewController: UIViewController {
    
    
    
    
    var answer: [Character] = []
    var word: [Character] = []
    var uppercasedWordList: [String] = []
    var columnCounter: Int = 0
    var rowCounter: Int = 1
    var letterBoxRow: [UILabel] = []
    var checkCounter: Int = 0 // plus 1 every time a box is set to blue, all correct when reached 5
    
    
    //Outlet collections.
    @IBOutlet var firstRowLetters: [UILabel]!
    @IBOutlet var secondRowLetters: [UILabel]!
    @IBOutlet var thirdRowLetters: [UILabel]!
    @IBOutlet var fourthRowLetters: [UILabel]!
    @IBOutlet var fifthRowLetters: [UILabel]!
    @IBOutlet var sixthRowLetters: [UILabel]!
    @IBOutlet var bottomRowButtons: [UIButton]!
    @IBOutlet weak var submitButton: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        //Found a txt word list (5757 words) at Github and read the words into an array and capitalize the words.
        var arrayOfWords: [String]?
        
        do {
            // This solution assumes  you've got the file in your bundle
            if let path = Bundle.main.path(forResource: "WordList", ofType: "txt"){
                let data = try String(contentsOfFile:path, encoding: String.Encoding.utf8)
                arrayOfWords = data.components(separatedBy: "\n")
                
            }
        } catch let err as NSError {
            // do something with Error
            print(err)
        }
        
        uppercasedWordList = arrayOfWords!.map {
            $0.uppercased()
        }
        
        reset()
        
        submitButton.isEnabled = false
    }
    
    // Every keyboard button is connected to this action.
    @IBAction func onButtonTapped(_ sender: UIButton) {
        
        if columnCounter < 5 { //limit word length to 5
            let letter: Character = Character(sender.titleLabel!.text!)
            //    if word.count < 5 {
            word.append(letter)
            //    }
            
            //Decide which row is currently used
            switch(rowCounter) {
            case 1: letterBoxRow = firstRowLetters
            case 2: letterBoxRow = secondRowLetters
            case 3: letterBoxRow = thirdRowLetters
            case 4: letterBoxRow = fourthRowLetters
            case 5: letterBoxRow = fifthRowLetters
            case 6: letterBoxRow = sixthRowLetters
            default: print("rowCounter out of range")
            }
            
            // Identify which box will take the letter and update its text with pressed button's text. UI labels are used to render the letter box. However, labels have to have text in them, otherwise they will be invisible. Have letter A in each of them to make boxes visible but the text's color is the same with background so the A will not be seen. When a letter is rendered, change color to black to make it visible.
            let letterBox: UILabel = letterBoxRow[columnCounter]
            letterBox.text = sender.titleLabel?.text
            letterBox.textColor = UIColor.black
            
            //When 5 letters are entered, check if the word is in the word list.
            if columnCounter == 4 {
                
                if !uppercasedWordList.contains(String(word)) {
                    
                    submitButton.setTitle("This is not a word!", for: UIControl.State.normal)
                    
                } else {
                    submitButton.isEnabled = true
                }
                
            }
            
            //Move to the next box when a keyboard button is pressed, until it reached 5.
            columnCounter = columnCounter + 1
            
        }
    }
    
    @IBAction func onDeleteTapped(_ sender: UIButton) {
        //When delete pressed, change the text color to hide it, rendering a deletion effect. Reverse other settings accordingly.
        if columnCounter > 0 {
            letterBoxRow[columnCounter-1].textColor = UIColor.lightGray
            word.removeLast()
            columnCounter = columnCounter - 1
            
            
            submitButton.isEnabled = false
            submitButton.setTitle("Submit A Word", for: UIControl.State.normal)        }
    }
    
    @IBAction func onSubmitButtonTapped(_ sender: UIButton) {
        //Use a checker to know when the correct word is guessed. (checkCounter == 5)
        checkCounter = 0
        let alertGood = UIAlertController(title: "Congratulations!", message: "You got the right word.Press the OK button to play again.", preferredStyle: .alert )
        let alertBad = UIAlertController(title: "You didn't get it.", message: "The answer is \'\(String(answer))\'. Press the OK button to try again.", preferredStyle: .alert )
        let windButton = UIAlertAction(title: "OK", style: .default) { _ in self.reset()}
        let loseButton = UIAlertAction(title: "OK", style: .default) { _ in self.reset()}
        alertGood.addAction(windButton)
        alertBad.addAction(loseButton)
        
        //Use these copies of the guess and answer, as some array item will be masked to avoid an letter in the answer being checked more than once.
        var tempAnswer: [Character] = answer
        var tempWord: [Character] = word
        
        
        
        
        //Check every index to determine the correctness. Blue color is straighforward, but the letter needs to be masked by assigning a "1" (or any other non alphabetical character) so that the next same letter in guess will not be checked against it and get an orange color.
        for i in 0...4 {
            if tempWord[i] == tempAnswer[i] {
                changeLetterBox(letterRow: letterBoxRow, index: i, c: word[i], color: UIColor.systemBlue)
                
                tempAnswer[i] = "1"
                checkCounter = checkCounter + 1
            }
            // Orange is a bit complex.
            else if tempAnswer.contains(tempWord[i]) {
                //If one letter is potentially orange ,need to check where it is and get the index, just in case there are more than one instance in the answer.
                let index = tempAnswer.firstIndex(of: tempWord[i])
                //Check if the instance is a blue, if so, mask it.
                if tempAnswer[index!] == tempWord[index!] {
                    tempAnswer[index!]="1"
                    tempWord[index!]="1"
                    //However, 2 scenarios after this. If there is more instance after the blue, this letter can be safely set to be orange. Else if this is the only one instance in the answer, once it is identified as blue, the previous orange has to be gray, as it is a premature orange.
                    if tempAnswer .contains(tempWord[i]){
                        changeLetterBox(letterRow: letterBoxRow, index: i, c: tempWord[i], color: UIColor.orange)
                        
                        //Find out the next instance after blue and mask it so it will be checked again.
                        let index = tempAnswer.firstIndex(of: tempWord[i])
                        tempAnswer[index!] = "2"
                        
                    } else {
                        
                        changeLetterBox(letterRow: letterBoxRow, index: i, c: tempWord[i], color: UIColor.darkGray)
                        
                    }
                    
                    //If there is no blue instance, safely set it to be orange.
                } else {
                    changeLetterBox(letterRow: letterBoxRow, index: i, c: tempWord[i], color: UIColor.orange)
                    tempAnswer[index!] = "2"
                    
                }
                
                //All the rest safely set to be gray.
                
            } else {
                changeLetterBox(letterRow: letterBoxRow, index: i, c: word[i], color: UIColor.darkGray)
            }
            
        }
        // Checker == 5, correct word was get, show alert and reset.
        if checkCounter == 5 {
            print("You got it!")
            self.show(alertGood, sender: nil)
        }
        // rows are finished but not get it, show sorry alert and reset.
        if rowCounter == 6 {
            print("You didn't make it.")
            self.show(alertBad, sender: nil)
        }
        
        //If not finised, win or not, reset for the next row.
        rowCounter = rowCounter + 1
        word = []
        columnCounter = 0
        submitButton.isEnabled = false
        
        print(tempAnswer, tempWord)
    }
    
    
    //Helper functions
    //Set colors for boxes based on the correcness when submit button pressed.
    
    func changeLetterBox(letterRow: [UILabel], index: Int, c: Character, color: UIColor) -> Void {
        letterRow[index].backgroundColor = color
        letterRow[index].text = String(c)
        letterRow[index].textColor = UIColor.white
        bottomRowButtons.forEach{button in
            if Character(button.titleLabel!.text!) == c {
                button.backgroundColor = color
            }
        }
    }
    //Reset when gamem finished, win or not.
    func reset() -> Void {
        
        self.bottomRowButtons.forEach{button in button.backgroundColor = UIColor.systemGray}
        let allLabels: [[UILabel]] = [self.firstRowLetters, self.secondRowLetters, self.thirdRowLetters, self.fourthRowLetters, self.fifthRowLetters,self.sixthRowLetters]
        for row in allLabels {
            for label in row {
                label.textColor = UIColor.lightGray
                label.backgroundColor = UIColor.lightGray
            }
        }
        createRandomWord()
        
        rowCounter = 1
    }
    
    //Create new work for guessing, called on game starting or resetting.
    
    func createRandomWord()->Void{
        
        // Get a random number to identify the word in the array
        let index: Int = Int(arc4random_uniform(5757))
        
        let wordString: String = uppercasedWordList[index]
        print("The selected word is: " + wordString)
        
        
        
        answer = [Character](wordString)
        print(answer)
        
    }
    
    
}


