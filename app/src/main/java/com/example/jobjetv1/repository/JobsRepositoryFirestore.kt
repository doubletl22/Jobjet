package com.example.jobjetv1.repository

import android.util.Log
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.data.model.JobPostUiState
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object JobsRepositoryFirestore {

    private val jobsCollection = FirebaseFirestore.getInstance().collection("jobs")

    suspend fun getAllJobs(): List<Job> {
        return try {
            jobsCollection.get().await().documents.mapNotNull { doc ->
                doc.toObject(Job::class.java)
            }
        } catch (e: Exception) {
            Log.e("FirestoreRepo", "Error getting all jobs", e)
            emptyList()
        }
    }


    suspend fun addJobFromPost(jobPostState: JobPostUiState): Job? {
        return try {
            // Tạo một document mới để lấy ID duy nhất
            val newJobDocument = jobsCollection.document()
            val jobId = newJobDocument.id

            // Chuyển đổi từ JobPostUiState sang đối tượng Job
            val newJob = Job(
                id = jobId,
                companyName = jobPostState.companyName,
                jobTitle = jobPostState.jobTitle,
                description = jobPostState.description,
                requirement = jobPostState.requirement,
                address = jobPostState.address,
                wage = "${jobPostState.wage} ${jobPostState.wageUnit}",
                quantity = jobPostState.quantity.toIntOrNull() ?: 0,
                workType = jobPostState.workType.name,
                contactName = jobPostState.contactName,
                contactEmail = jobPostState.contactEmail,
                contactPhone = jobPostState.contactPhone
                // postedDate sẽ lấy thời gian hiện tại theo giá trị mặc định
            )

            // Lưu vào Firestore và đợi hoàn thành
            newJobDocument.set(newJob).await()
            Log.d("FirestoreRepo", "Job added successfully with ID: $jobId")

            newJob // Trả về đối tượng Job vừa tạo

        } catch (e: Exception) {
            Log.e("FirestoreRepo", "Error adding job to Firestore", e)
            null // Trả về null nếu có lỗi
        }
    }
}
