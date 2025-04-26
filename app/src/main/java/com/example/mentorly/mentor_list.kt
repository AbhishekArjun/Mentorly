package com.example.mentorly

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class mentor_list : AppCompatActivity() {

    // list of mentors
    private val mentorList = listOf(
        Mentor(
            "Anupam Mittal",
            R.drawable.anupam_mittal,
            "Creative Director, passionate about helping young designers improve their skills and reach their potential.",
            arrayOf("Leadership", "Creative Direction", "Design Strategy"),
            arrayOf("Sarah is an amazing mentor who really took the time to understand my goals and help me achieve them. - Jane")
        ),
        Mentor(
            "Aman Gupta",
            R.drawable.aman_gupta,
            "Co-Founder & CMO at boAt Lifestyle. Known for his marketing strategies and brand building.",
            arrayOf("Marketing", "Branding", "E-commerce"),
            arrayOf("Aman's insights on marketing were invaluable. - Priya")
        ),
        Mentor(
            "Ashneer Grover",
            R.drawable.ashneer_grover,
            "Co-Founder of BharatPe. Known for his direct and often blunt business advice.",
            arrayOf("Business Strategy", "Finance", "Entrepreneurship"),
            arrayOf("Ashneer's feedback was tough but very helpful. - Rohan")
        ),
        Mentor(
            "Peyush Bansal",
            R.drawable.peyush_bansal,
            "Founder & CEO of Lenskart. A visionary leader in the eyewear industry.",
            arrayOf("E-commerce", "Retail", "Business Development"),
            arrayOf("Peyush provided great guidance on scaling my business. - Kavita")
        ),
        Mentor(
            "Vineeta Singh",
            R.drawable.vineeta_singh,
            "CEO & Co-Founder of SUGAR Cosmetics. An inspiring entrepreneur in the beauty industry.",
            arrayOf("Beauty Industry", "Marketing", "Brand Building"),
            arrayOf("Vineeta's journey is truly inspiring, and her advice was practical. - Sunil")
        ),
        Mentor(
            "Namita Thapar",
            R.drawable.namita_thapar,
            "Executive Director of Emcure Pharmaceuticals. Experienced in healthcare and business management.",
            arrayOf("Healthcare", "Pharmaceuticals", "Business Management"),
            arrayOf("Namita's understanding of the healthcare sector was very beneficial. - Meera")
        ),
        Mentor(
            "Ghazal Alagh",
            R.drawable.ghazal_alagh,
            "Co-Founder & Chief Mama at Mamaearth. Focused on natural and safe baby care products.",
            arrayOf("Consumer Goods", "Marketing", "E-commerce"),
            arrayOf("Ghazal's insights into the consumer market were eye-opening. - Vikram")
        ),
        Mentor(
            "Ritesh Agarwal",
            R.drawable.ritesh_agarwal,
            "Founder & CEO of OYO Rooms. A young and successful entrepreneur in the hospitality industry.",
            arrayOf("Hospitality", "Business Growth", "Startups"),
            arrayOf("Ritesh's story motivated me to pursue my startup dreams. - Anjali")
        ),
        Mentor(
            "Dipender Goyal",
            R.drawable.dipender_goyal,
            "Founder & CEO of Zomato. Revolutionizing the food delivery and restaurant discovery space.",
            arrayOf("Food Tech", "E-commerce", "Logistics"),
            arrayOf("Dipender's vision for Zomato is truly remarkable. - Saurabh")
        ),
        Mentor(
            "Amit Jain",
            R.drawable.amit_jain,
            "Co-Founder & CEO of CarDekho Group. Transforming the online car buying experience.",
            arrayOf("Automotive Industry", "E-commerce", "Technology"),
            arrayOf("Amit's expertise in the online marketplace was very helpful. - Shreya")
        )
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mentor_list)

        val mentorLinearLayout = findViewById<LinearLayout>(R.id.mentorLinearLayout)

        for (i in 0 until mentorLinearLayout.childCount) {
            val cardView = mentorLinearLayout.getChildAt(i)
            if (cardView is CardView) {
                cardView.setOnClickListener {
                    val selectedMentor = mentorList[i]
                    val intent = Intent(this, mentordetailspage::class.java)
                    intent.putExtra("mentor_name", selectedMentor.name)
                    intent.putExtra("mentor_image", selectedMentor.imageResourceId)
                    intent.putExtra("mentor_description", selectedMentor.description)
                    intent.putExtra("mentor_expertise", selectedMentor.expertise)
                    intent.putExtra("mentor_reviews", selectedMentor.reviews)
                    startActivity(intent)
                }
            }
        }
    }
}

data class Mentor(
    val name: String,
    val imageResourceId: Int,
    val description: String,
    val expertise: Array<String>,
    val reviews: Array<String>
)