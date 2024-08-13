package com.example.custom_contact.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.custom_contact.domain.model.Contact
import com.example.custom_contact.domain.usecase.AddContactUseCase
import com.example.custom_contact.domain.usecase.GetContactsUseCase
import com.example.custom_contact.domain.usecase.SearchContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase,
    private val searchContactsUseCase: SearchContactsUseCase,
    private val addContactUseCase: AddContactUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val contacts: StateFlow<List<Contact>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isEmpty()) {
                getContactsUseCase()
            } else {
                searchContactsUseCase(query)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            initializeContacts()
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    private suspend fun initializeContacts() {
        val currentContacts = getContactsUseCase().stateIn(viewModelScope).value
        if (currentContacts.isEmpty()) {
            val initialContacts = generateInitialContacts()
            initialContacts.forEach { contact ->
                addContactUseCase(contact)
            }
        }
    }

    private fun generateInitialContacts(): List<Contact> {
        return listOf(
            Contact(name="Kimberlee Turlington", phoneNumber="039 298-72-30", email="abc@gmail.com", relationship="friend"),
            Contact(name="Miguel Eveland", phoneNumber="032-2659094", email="abc@mail.com", relationship="friend"),
            Contact(name="Audrie Smid", phoneNumber="03758295-10 32", email="abc@mail.com", relationship="friend"),
            Contact(name="Antonia Maslanka", phoneNumber="00594-83-34 93", email="abc@mail.com", relationship="friend"),
            Contact(name="Val Hoffmeyer", phoneNumber="001-859-07-84", email="abc@mail.com", relationship="friend"),
            Contact(name="Fleta Mckiney", phoneNumber="046-30-01-80", email="abc@mail.com", relationship="friend"),
            Contact(name="Leanna Wedel", phoneNumber="0184 425384", email="abc@mail.com", relationship="friend"),
            Contact(name="Reva Larger", phoneNumber="0262-2875", email="abc@mail.com", relationship="friend"),
            Contact(name="Shelby Prator", phoneNumber="0174-1599", email="abc@mail.com", relationship="friend"),
            Contact(name="Micheal Veronesi", phoneNumber="09564 310-6 4", email="abc@mail.com", relationship="friend"),
            Contact(name="Tyrone Hopton", phoneNumber="00718 04608-5", email="abc@mail.com", relationship="friend"),
            Contact(name="Cheri Batson", phoneNumber="06243158218-21", email="abc@mail.com", relationship="friend"),
            Contact(name="Sol Stockfisch", phoneNumber="092409-2 75", email="abc@mail.com", relationship="friend"),
            Contact(name="Briana Angry", phoneNumber="04893-326-429", email="abc@mail.com", relationship="friend"),
            Contact(name="Elfreda Capron", phoneNumber="02091941 2138", email="abc@mail.com", relationship="friend"),
            Contact(name="Hannah Perloff", phoneNumber="089721 796 492", email="abc@mail.com", relationship="friend"),
            Contact(name="Chanell Neidlinger", phoneNumber="01596 197 34-92", email="abc@mail.com", relationship="friend"),
            Contact(name="Shaniqua Khan", phoneNumber="09376715-52 9", email="abc@mail.com", relationship="friend"),
            Contact(name="Stewart Lenzo", phoneNumber="06251 96-5-75", email="abc@mail.com", relationship="friend"),
            Contact(name="Rosalina Merthie", phoneNumber="035490 054 8442", email="abc@mail.com", relationship="friend"),
            Contact(name="Cher Griswould", phoneNumber="026-67-976", email="abc@mail.com", relationship="friend"),
            Contact(name="Sonia Otsuka", phoneNumber="004 20903 95", email="abc@mail.com", relationship="friend"),
            Contact(name="Benny Graden", phoneNumber="05263 82461-31", email="abc@mail.com", relationship="friend"),
            Contact(name="Cyrus Balius", phoneNumber="0089-57289-0", email="abc@mail.com", relationship="friend"),
            Contact(name="Shanon Kueny", phoneNumber="071 20 30 2", email="abc@mail.com", relationship="friend"),
            Contact(name="Walton Svoboda", phoneNumber="089 203-58 03", email="abc@mail.com", relationship="friend"),
            Contact(name="Daron Gardocki", phoneNumber="0351 560-753", email="abc@mail.com", relationship="friend"),
            Contact(name="Stephine Daum", phoneNumber="06831402897-92", email="abc@mail.com", relationship="friend"),
            Contact(name="Alva Sauvageau", phoneNumber="045-5177 0", email="abc@mail.com", relationship="friend"),
            Contact(name="Silas Scarth", phoneNumber="053618 0370517", email="abc@mail.com", relationship="friend"),
            Contact(name="Diego Schaefer", phoneNumber="056872 42-25", email="abc@mail.com", relationship="friend"),
            Contact(name="Shenita Broxterman", phoneNumber="0796-92 7309", email="abc@mail.com", relationship="friend"),
            Contact(name="Lucas Trischitta", phoneNumber="0853-17-7-6", email="abc@mail.com", relationship="friend"),
            Contact(name="Felisa Burmeister", phoneNumber="0702-0676 82", email="abc@mail.com", relationship="friend"),
            Contact(name="Latricia Fickas", phoneNumber="049356 527-27-69", email="abc@mail.com", relationship="friend"),
            Contact(name="Usha Horsely", phoneNumber="039-70-40 60", email="abc@mail.com", relationship="friend"),
            Contact(name="Augustine Crosswell", phoneNumber="0305 20439-38", email="abc@mail.com", relationship="friend"),
            Contact(name="Kandis Netherland", phoneNumber="006978 15 1821", email="abc@mail.com", relationship="friend"),
            Contact(name="Emile Steenburg", phoneNumber="0208 04506", email="abc@mail.com", relationship="friend"),
            Contact(name="Tomi Minasian", phoneNumber="0049 6316259", email="abc@mail.com", relationship="friend"),
            Contact(name="Rusty Biafore", phoneNumber="021069 46 5834", email="abc@mail.com", relationship="friend"),
            Contact(name="Art Esperanza", phoneNumber="095438918516", email="abc@mail.com", relationship="friend"),
            Contact(name="Keneth Sigg", phoneNumber="0235038 8692", email="abc@mail.com", relationship="friend"),
            Contact(name="Gilda Wilbers", phoneNumber="02765 5127872", email="abc@mail.com", relationship="friend"),
            Contact(name="Marhta Genther", phoneNumber="0830294174 0", email="abc@mail.com", relationship="friend"),
            Contact(name="Vicente Perko", phoneNumber="042 542 89 21", email="abc@mail.com", relationship="friend"),
            Contact(name="Amalia Velie", phoneNumber="0233050 4", email="abc@mail.com", relationship="friend"),
            Contact(name="Bruno Feeley", phoneNumber="062375-068-52 89", email="abc@mail.com", relationship="friend"),
            Contact(name="Frankie Mires", phoneNumber="0351 17-40 41", email="abc@mail.com", relationship="friend"),
            Contact(name="Samual Plattsmier", phoneNumber="059-175 871", email="abc@mail.com", relationship="friend"),
            Contact(name="Alejandro Forquer", phoneNumber="04 079-50 17", email="abc@mail.com", relationship="friend"),
            Contact(name="Isaac Beauchemin", phoneNumber="03506038-52-3", email="abc@mail.com", relationship="friend"),
            Contact(name="Ashley Mckellan", phoneNumber="090 8468-1", email="abc@mail.com", relationship="friend"),
            Contact(name="Jolie Kiflezghie", phoneNumber="021-617-69-72", email="abc@mail.com", relationship="friend"),
            Contact(name="Marna Pullom", phoneNumber="009842517 8 1", email="abc@mail.com", relationship="friend"),
            Contact(name="Chong Kudrick", phoneNumber="021-742 25 59", email="abc@mail.com", relationship="friend"),
            Contact(name="Jamison Severs", phoneNumber="085-031-4296", email="abc@mail.com", relationship="friend"),
            Contact(name="Griselda Levels", phoneNumber="03045-193 97-61", email="abc@mail.com", relationship="friend"),
            Contact(name="Norah Gildow", phoneNumber="0246-24-2-17", email="abc@mail.com", relationship="friend"),
            Contact(name="Renda Houskeeper", phoneNumber="0293-06 1", email="abc@mail.com", relationship="friend"),
            Contact(name="Roselee Gwynn", phoneNumber="025368 084-985", email="abc@mail.com", relationship="friend"),
            Contact(name="Francesco Pera", phoneNumber="076019 168 24-53", email="abc@mail.com", relationship="friend"),
            Contact(name="Janelle Gadberry", phoneNumber="030947-48235 46", email="abc@mail.com", relationship="friend"),
            Contact(name="Riva Richner", phoneNumber="070336 25-02", email="abc@mail.com", relationship="friend"),
            Contact(name="Annamarie Devendorf", phoneNumber="068-631-47", email="abc@mail.com", relationship="friend"),
            Contact(name="Marty Robinson", phoneNumber="0596-14 16 24", email="abc@mail.com", relationship="friend"),
            Contact(name="Glen Raybourn", phoneNumber="040720417 13", email="abc@mail.com", relationship="friend"),
            Contact(name="Candance Lynds", phoneNumber="03928 0419690", email="abc@mail.com", relationship="friend"),
            Contact(name="Houston Ezparza", phoneNumber="08073-29 0832", email="abc@mail.com", relationship="friend"),
            Contact(name="Arnoldo Galamay", phoneNumber="0093486 71-1", email="abc@mail.com", relationship="friend"),
            Contact(name="Lashonda Weidman", phoneNumber="0342 156-5 90", email="abc@mail.com", relationship="friend"),
            Contact(name="Hershel Defilippo", phoneNumber="04671037-0180", email="abc@mail.com", relationship="friend"),
            Contact(name="Bruno Vue", phoneNumber="020 1763 14", email="abc@mail.com", relationship="friend"),
            Contact(name="Leslee Emge", phoneNumber="0584 081 8 49", email="abc@mail.com", relationship="friend"),
            Contact(name="Leon Mondy", phoneNumber="04265 804-06-60", email="abc@mail.com", relationship="friend"),
            Contact(name="Charlie Medel", phoneNumber="01-476 20 60", email="abc@mail.com", relationship="friend"),
            Contact(name="Donnette Lashbrook", phoneNumber="0514 806", email="abc@mail.com", relationship="friend"),
            Contact(name="Alvaro Colfer", phoneNumber="047535660 48", email="abc@mail.com", relationship="friend"),
            Contact(name="Letisha Reindel", phoneNumber="013 04679-69", email="abc@mail.com", relationship="friend"),
            Contact(name="Jake Quinney", phoneNumber="01082 7831669", email="abc@mail.com", relationship="friend"),
            Contact(name="Gayle Avans", phoneNumber="029-326 90-62", email="abc@mail.com", relationship="friend"),
            Contact(name="Jeffery Hansbrough", phoneNumber="024 613-28 89", email="abc@mail.com", relationship="friend"),
            Contact(name="Lenny Redenbaugh", phoneNumber="034203 398", email="abc@mail.com", relationship="friend"),
            Contact(name="Jude Alvey", phoneNumber="096582241-8372", email="abc@mail.com", relationship="friend"),
            Contact(name="Minh Dionisio", phoneNumber="0475349 24 27", email="abc@mail.com", relationship="friend"),
            Contact(name="Huey Twedell", phoneNumber="0507-197-0", email="abc@mail.com", relationship="friend"),
            Contact(name="Jacquie Burbach", phoneNumber="0326-58-09 0", email="abc@mail.com", relationship="friend"),
            Contact(name="Orpha Geiss", phoneNumber="02380875348", email="abc@mail.com", relationship="friend"),
            Contact(name="Denese Escovedo", phoneNumber="065 709-86", email="abc@mail.com", relationship="friend"),
            Contact(name="Demarcus Dittrich", phoneNumber="048-0795635", email="abc@mail.com", relationship="friend"),
            Contact(name="Maximo Micco", phoneNumber="06243-39 32 98", email="abc@mail.com", relationship="friend"),
            Contact(name="Jc Zilnicki", phoneNumber="008975 735 2 08", email="abc@mail.com", relationship="friend"),
            Contact(name="Britt Savin", phoneNumber="001-5813 0", email="abc@mail.com", relationship="friend"),
            Contact(name="Pasty Garbett", phoneNumber="07049 16 41", email="abc@mail.com", relationship="friend"),
            Contact(name="Ernie Hegan", phoneNumber="027 249-84-12", email="abc@mail.com", relationship="friend"),
            Contact(name="Rolland Halsey", phoneNumber="093 284 1 45", email="abc@mail.com", relationship="friend"),
            Contact(name="Stevie Strebeck", phoneNumber="01306 963 93-60", email="abc@mail.com", relationship="friend"),
            Contact(name="Marylin Applegate", phoneNumber="09412 572 92-58", email="abc@mail.com", relationship="friend"),
            Contact(name="Stacia Kelleher", phoneNumber="001969147523", email="abc@mail.com", relationship="friend"),
            Contact(name="Khadijah Rouleau", phoneNumber="08596 470 6781", email="abc@mail.com", relationship="friend"),
            Contact(name="Lincoln Orantes", phoneNumber="01276645-83 91", email="abc@mail.com", relationship="friend"),
            Contact(name="Lindsey Vanvleck", phoneNumber="0547-54023-17", email="abc@mail.com", relationship="friend"),
            Contact(name="Tommie Diviney", phoneNumber="098357 184", email="abc@mail.com", relationship="friend"),
            Contact(name="Domenic Schwiebert", phoneNumber="026017054 1 79", email="abc@mail.com", relationship="friend"),
            Contact(name="Novella Sniezek", phoneNumber="0550682 06", email="abc@mail.com", relationship="friend"),
            Contact(name="Veronika Maisonet", phoneNumber="040 645 07 28", email="abc@mail.com", relationship="friend"),
            Contact(name="Nicky Schulweis", phoneNumber="034791-975 3739", email="abc@mail.com", relationship="friend"),
            Contact(name="Eileen Spearin", phoneNumber="04236576-19-3", email="abc@mail.com", relationship="friend"),
            Contact(name="Warner Cotty", phoneNumber="046981-187387", email="abc@mail.com", relationship="friend"),
            Contact(name="Gita Hogle", phoneNumber="035 54-61 07", email="abc@mail.com", relationship="friend"),
            Contact(name="Isa Debiasio", phoneNumber="089869-67-34", email="abc@mail.com", relationship="friend"),
            Contact(name="Manie Colafrancesco", phoneNumber="042955475", email="abc@mail.com", relationship="friend"),
            Contact(name="Kraig Millsap", phoneNumber="0647-94-36-43", email="abc@mail.com", relationship="friend"),
            Contact(name="Armando Nass", phoneNumber="09612 03 2395", email="abc@mail.com", relationship="friend"),
            Contact(name="Lou Mou", phoneNumber="0682 037-191", email="abc@mail.com", relationship="friend"),
            Contact(name="Erwin Halferty", phoneNumber="0742-509 53 41", email="abc@mail.com", relationship="friend"),
            Contact(name="Desmond Ruano", phoneNumber="0845926-37 39", email="abc@mail.com", relationship="friend"),
            Contact(name="Lawrence Bottom", phoneNumber="010052-85-92", email="abc@mail.com", relationship="friend"),
            Contact(name="Colette Irmeger", phoneNumber="061 832 07-35", email="abc@mail.com", relationship="friend"),
            Contact(name="Holley Mandrell", phoneNumber="042360 05232 14", email="abc@mail.com", relationship="friend"),
            Contact(name="Latrisha Trotman", phoneNumber="003597 69170 74", email="abc@mail.com", relationship="friend"),
            Contact(name="Dorsey Bertling", phoneNumber="02056207 451", email="abc@mail.com", relationship="friend"),
            Contact(name="Ronny Knoth", phoneNumber="08302 032-61-50", email="abc@mail.com", relationship="friend"),
            Contact(name="Sondra Spilker", phoneNumber="071 765-53-80", email="abc@mail.com", relationship="friend"),
            Contact(name="Theodore Ty", phoneNumber="07814 352-7 85", email="abc@mail.com", relationship="friend"),
            Contact(name="Donald Borgman", phoneNumber="0643 19 4761", email="abc@mail.com", relationship="friend"),
            Contact(name="Cortez Rodeheaver", phoneNumber="0297625-9 65", email="abc@mail.com", relationship="friend"),
            Contact(name="Tracy Weisenfels", phoneNumber="06304-62 60", email="abc@mail.com", relationship="friend"),
            Contact(name="Wilbert Cardonia", phoneNumber="0543615 01 62", email="abc@mail.com", relationship="friend"),
            Contact(name="Soila Queja", phoneNumber="047-8091037", email="abc@mail.com", relationship="friend"),
            Contact(name="Evelina Barge", phoneNumber="0972 160 20 2", email="abc@mail.com", relationship="friend"),
            Contact(name="Tisha Mccarry", phoneNumber="090073-63-42", email="abc@mail.com", relationship="friend")
        )
    }
}