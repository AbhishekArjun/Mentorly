import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.mentorly.OnboardingItem
import com.example.mentorly.R

class OnboardingAdapter(
    private val items: List<OnboardingItem>,
    private val onLastPageButtonClick: () -> Unit
) : RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>() {

    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val button: Button = itemView.findViewById(R.id.btnGetStarted)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.imageView.setImageResource(items[position].imageResId)

        // Last image pe button dikhana
        if (position == items.size - 1) {
            holder.button.visibility = View.VISIBLE
            holder.button.setOnClickListener {
                onLastPageButtonClick()
            }
        } else {
            holder.button.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size
}
