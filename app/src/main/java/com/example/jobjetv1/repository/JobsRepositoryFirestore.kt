package com.example.jobjetv1.repository

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.data.model.JobPostUiState
import com.example.jobjetv1.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

object JobsRepositoryFirestore {

    private val firestore = FirebaseFirestore.getInstance()
    private const val JOBS_COLLECTION = "jobs"

    private var _allJobs by mutableStateOf(listOf<Job>())
    val allJobs: List<Job> get() = _allJobs

    private var listenerRegistration: ListenerRegistration? = null

    init {
        // Start listening to Firestore jobs collection
        listenToJobsUpdates()
    }

    private fun listenToJobsUpdates() {
        listenerRegistration = firestore.collection(JOBS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Handle error if needed
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val jobs = snapshot.documents.mapNotNull { doc ->
                        try {
                            val jobData = doc.toObject<JobFirestoreData>()
                            jobData?.toJob(doc.id)
                        } catch (e: Exception) {
                            null
                        }
                    }
                    _allJobs = jobs.sortedByDescending { it.id.toIntOrNull() ?: 0 }
                }
            }
    }

    suspend fun addJobToFirestore(jobPost: JobPostUiState): Job? {
        val jobData = JobFirestoreData.fromJobPost(jobPost)
        return try {
            val docRef = firestore.collection(JOBS_COLLECTION).add(jobData).await()
            val newJob = jobData.toJob(docRef.id)
            _allJobs = listOf(newJob) + _allJobs
            newJob
        } catch (e: Exception) {
            null
        }
    }

    fun getJobById(id: String): Job? {
        return _allJobs.find { it.id == id }
    }

    private fun getIconForJobType(jobTitle: String): Int {
        return when {
            jobTitle.contains("kho", ignoreCase = true) -> R.drawable.outline_warehouse_24
            jobTitle.contains("nhà hàng", ignoreCase = true) ||
            jobTitle.contains("phục vụ", ignoreCase = true) -> R.drawable.outline_restaurant_24
            jobTitle.contains("bán hàng", ignoreCase = true) ||
            jobTitle.contains("shop", ignoreCase = true) -> R.drawable.ic_launcher_foreground
            jobTitle.contains("lao động", ignoreCase = true) ||
            jobTitle.contains("công nhân", ignoreCase = true) -> R.drawable.outline_tools_pliers_wire_stripper_24
            else -> R.drawable.outline_warehouse_24
        }
    }

    data class JobFirestoreData(
        val companyName: String = "",
        val jobTitle: String = "",
        val quantity: String = "",
        val workType: String = "",
        val wage: String = "",
        val wageUnit: String = "",
        val description: String = "",
        val requirement: String = "",
        val rule: String = "",
        val address: String = "",
        val contactName: String = "",
        val contactEmail: String = "",
        val contactPhone: String = ""
    ) {
        fun toJob(id: String): Job {
            val wageStr = "$wage $wageUnit"
            val iconRes = getIconForJobType(jobTitle)
            return Job(
                id = id,
                title = jobTitle,
                address = address,
                description = description,
                wage = wageStr,
                wageColor = Color(0xFF43A047),
                iconRes = iconRes
            )
        }

        companion object {
            fun fromJobPost(jobPost: JobPostUiState): JobFirestoreData {
                return JobFirestoreData(
                    companyName = jobPost.companyName,
                    jobTitle = jobPost.jobTitle,
                    quantity = jobPost.quantity,
                    workType = jobPost.workType.name,
                    wage = jobPost.wage,
                    wageUnit = jobPost.wageUnit,
                    description = jobPost.description,
                    requirement = jobPost.requirement,
                    rule = jobPost.rule,
                    address = jobPost.address,
                    contactName = jobPost.contactName,
                    contactEmail = jobPost.contactEmail,
                    contactPhone = jobPost.contactPhone
                )
            }
        }
    }
}
