
#include "stdafx.h"
#include <windows.h>
#include <iostream>
#include <time.h>
#include <ctime>
#include <vector>
#include <string>

using namespace std;

//klasa osoba - początek
class Human
{
public:

	enum eGender	//typ enum dla ustawienia płci
	{
		NOSET,
		FEMALE,
		MALE,
	};

	struct Date		//struktura dla daty urodzenia
	{
	private:
		static const Date sIncorrectDate;	//wartość const - nieprawidłowa data

	public:
		static Date getIncorrectDate()
		{
			return sIncorrectDate;
		}

		//zmienne
		int	_year, _month, _day;

		// konstruktor daty
		Date(int day, int month, int year) :
			_day(day)
			, _month(month)
			, _year(year)
		{}
	};

private:
	eGender		m_gender;			 //płeć
	char		m_name[31];			 //imię
	char		m_surname[31];		 //nazwisko
	Date		m_birthDayDate;		 //data urodzenia


	int calculateAge() const		//oblicza wiek
	{
		time_t now = time(0);		//rzeczywisty czas i data
		tm *ltm = new tm();			//struktura dla daty oraz czasu
		localtime_s(ltm, &now);		//dajemy czas do struktury
		int y = 1900 + ltm->tm_year;// rok
		int m = 1 + ltm->tm_mon;	// miesiąc
		int d = ltm->tm_mday;		// dzień
		int y1 = y - m_birthDayDate._year;
		if (m < m_birthDayDate._month || (m == m_birthDayDate._month && d < m_birthDayDate._day))
			y1--;
		return y1;
	}

	void setName()					// wprowadzenie imienia
	{
		cout << "Name: ";
		cin >> m_name;
	}

	void setSurname()				// wprowadzenie nazwiska
	{
		cout << "Surname: ";
		cin >> m_surname;
	}

	void setBirthdayDate()			// wprowadzenie daty urodzenia
	{
		int day, month, year;
		cout << "Date of Birth\n";
		cout << "year: ";
		cin >> year;
		cout << "month: ";
		cin >> month;
		cout << "day: ";
		cin >> day;
		m_birthDayDate = Date(day, month, year);
	}

	void setGender(eGender gender)	// wprowadzenie płci osoby
	{
		m_gender = gender;
	}

public:

	Human() :	// konstruktor osoby 
		m_gender(NOSET),
		m_birthDayDate(Date::getIncorrectDate())
	{
		m_name[0] = 0;
		m_surname[0] = 0;
	}


	~Human()	// destruktor osoby
	{
	}

	void set()	// zapisać imię i nazwisko osoby
	{
		setName();
		setSurname();
	}
	void set(eGender gender) //podać imię, nazwisko i datę urodzenia osoby o określonej płci
	{
		setGender(gender);
		setName();
		setSurname();
		setBirthdayDate();
	}

	bool compare(Human chuman) // porównać "osobę" z imieniem i nazwiskiem (true - gdy wszystko się zgadza)
	{
		return (!strcmp(chuman.m_name, m_name) && !strcmp(chuman.m_surname, m_surname));
	}

	int getAge()	// obliczanie wieku
	{
		return calculateAge();
	}

	void print()	// wydruk informacji dotyczącej osoby
	{
		switch (m_gender) { // Pani czy Pan
		case FEMALE:
			cout << "Ms. ";
			break;
		case MALE:
			cout << "Mr. ";
			break;
		}
		cout << m_name << " " << m_surname << ", age " << getAge(); // имя фамилия, возраст
	}

	eGender genderMenu()	//podaje menu osoby i zwraca numer warianta
	{
		int sel;
		cout << "1. MALE\n";
		cout << "2. FEMALE\n";
		cout << "Choose a gender:\n";
		cout << "> ";
		cin >> sel;
		switch (sel)
		{
		case 1:
			return MALE;
		case 2:
			return FEMALE;
		default:
			return NOSET;
		}
	}

};
// klasa osoba - koniec

const Human::Date Human::Date::sIncorrectDate = Date(-1, -1, -1);  // wprowadzamy wartość const dla daty

// klasa specjalista - początek
class SpecialHuman : public Human  // na podstawie klasy "osoba" tworzymy klasę "specjalista" drużyny (teamu)
{
public:
	enum eSpecial	// typ wyliczeniowy dla określenia roli w drużynie
	{
		NOSET,
		DIRECTOR,
		TRAINER,
		GOALKEEPER,
		DEFENDER,
		HALTERMAN,
		ATTACKER,
	};

private:
	eSpecial m_special;	// specjalizacja

	void setGender(const eSpecial& set)	// podanie specjalizacji
	{
		m_special = set;
	}

public:

	SpecialHuman() :		// konstruktor specjalisty
		m_special(NOSET)
	{
	}

	~SpecialHuman()		// destruktor specjalisty
	{
	}

	void set()			// Zapisanie informacji o nieokreślonym specjaliście
	{
		Human::set();
	}
	void set(eGender gender, eSpecial special) // Zapisanie informacji o określonym specjaliście o określonej płci
	{
		m_special = special;
		Human::set(gender);
	}

	eSpecial getSpecial()	// zwraca kod specjalizacji
	{
		return m_special;
	}

	void print()	// drukuje informację o osobie
	{
		Human::print();     // najpierw jako o osobie

		switch (m_special)	// a potem podaje jego specjalność
		{
		case DIRECTOR:
			cout << ", Director";
			break;
		case TRAINER:
			cout << ", Trainer";
			break;
		case GOALKEEPER:
			cout << ", Goalkeeper";
			break;
		case DEFENDER:
			cout << ", Defender";
			break;
		case HALTERMAN:
			cout << ", Halterman";
			break;
		case ATTACKER:
			cout << ", Attacker";
			break;
		default:
			break;
		}
	}

	eSpecial specialMenu()	//podaje menu graczy drużyny i zwraca numer wariantu
	{
		int sel;
		cout << "1. DIRECTOR\n";
		cout << "2. TRAINER\n";
		cout << "3. GOALKEEPER\n";
		cout << "4. DEFENDER\n";
		cout << "5. HALTERMAN\n";
		cout << "6. ATTACKER\n";
		cout << "0. Cancel\n";
		cout << "Choose a specialist team:\n";
		cout << "> ";
		cin >> sel;
		switch (sel)
		{
		case 1:
			return DIRECTOR;
		case 2:
			return TRAINER;
		case 3:
			return GOALKEEPER;
		case 4:
			return DEFENDER;
		case 5:
			return HALTERMAN;
		case 6:
			return ATTACKER;
		default:
			return NOSET;
		}
	}
};
//klasa specjalista - koniec


// klasa drużyna (team) - początek
class Team
{
private:
	char		t_name[31];			// nazwa drużyny
	vector<SpecialHuman*> TeamList; // wektor - spis drużyny
	SpecialHuman* special;			//wskaźnik na egzemplarz klasy specjalista
	SpecialHuman vspecial;			//egzemplarz klasy specjalista

public:
	void setName()					// wprowadź imię
	{
		cout << "Team name: ";
		cin >> t_name;
	}

	string getName()
	{
		return t_name;
	}

	void add()
	{
		special = new SpecialHuman();
		special->set(special->genderMenu(), special->specialMenu());
		TeamList.push_back(special);
	}

	void del()
	{
		vspecial.set();	//wypełniamy egzemplarz specjalisty przez imię i nazwisko
		unsigned int i = 0;			//na początek listy drużyny
		while (i < TeamList.size() && !TeamList[i]->compare(vspecial)) //póki nie dotrzemy końca i imię nie będzie się zgadzało z nazwiskiem
		{
			i++;		// ruszamy dalej
		}
		if (i < TeamList.size()) {	//jeżeli nie koniec, to znaleźliśmy zgodność
			cout << i << ": ";	// znalezione drukujemy na ekran
			TeamList[i]->print();
			cout << "\nDo you really want to delete this?(Y/N) ";
			char choice;
			cin >> choice;
			if (choice == 'Y' || choice == 'y') {
				special = TeamList[i];
				TeamList.erase(TeamList.begin() + i);
				delete(special);
				cout << "Deleted!\n";
			}
		}
		else {
			cout << "Not found!\n";
		}
	}

	void delAll()
	{
		if (TeamList.size())
			for (unsigned int i = 0; i < TeamList.size(); i++)
			{
				delete(TeamList[i]);
			}
	}

	void print()
	{
		cout << "\nTeam \"" << getName() << "\":\n";
		if (TeamList.size())
			for (unsigned int i = 0; i < TeamList.size(); i++) //cykl po całej liście
			{
				cout << i << ": ";	 //wyprowadzamy informację na ekran
				TeamList[i]->print();
				cout << "\n";
			}
		else
			cout << "< nothing is there >\n";
	}

	void print(SpecialHuman::eSpecial special)
	{
		cout << "\nTeam \"" << getName() << "\":\n";
		if (TeamList.size())
			for (unsigned int i = 0; i < TeamList.size(); i++) //cykl po całej liście
			{
				if (TeamList[i]->getSpecial() == special) //jeżeli specjalista odpowiada wyboru
				{
					cout << i << ": ";	//wyprowadzamy informację na ekran
					TeamList[i]->print();
					cout << "\n";
				}
			}
		else
			cout << "< nothing is there >\n";
	}
};
// klasa drużyna - koniec

//wyprowadza menu główne i zwraca numer wariantu
int MainMenu()
{
	unsigned int sel;
	cout << "\n\nMAIN MENU\n";
	cout << "1. Add specialist team\n";
	cout << "2. Remove specialist team\n";
	cout << "3. Print the entire team list\n";
	cout << "4. Print selective team list\n";
	cout << "0. Exit\n";
	cout << "Select an action:\n";
	cout << "> ";
	cin >> sel;
	return sel;
}

int _tmain(int argc, TCHAR*argv[])
{
	Team myTeam;				//egzemplarz klasy drużyna
	SpecialHuman vspecial;		//egzemplarz klasy specjalista

	myTeam.setName();			//wprowadzamy imię drużyny

	unsigned int sel;			// selektor menu

	do
	{
		sel = MainMenu();		//wywołujmy główne menu
		switch (sel)
		{
		case 1:				// jeżeli naciśniemy 1
			cout << "\nAdd specialist...\n";
			myTeam.add();
			break;
		case 2:				// jeżeli naciśniemy 2
			cout << "\nRemove specialist...\n";
			myTeam.del();
			break;
		case 3:
			cout << "\nPrint the entire team list...\n";
			myTeam.print();
			break;
		case 4:
			cout << "\nPrint selective team list...\n";
			myTeam.print(vspecial.specialMenu());	//pytamy jakich specjalistów ma zwrócić i drukujemy
			break;
		default:
			cout << "\nYou chose the wrong option!\n\n";
		}
	} while (sel);  // póki nie zero (nie ma wyjścia)

	cout << "\nExit...\n";

	return 0;
}