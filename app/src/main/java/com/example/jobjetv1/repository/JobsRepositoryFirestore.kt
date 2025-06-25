package com.example.jobjetv1.repository

import android.util.Log
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.data.model.JobPostUiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
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

            // Tạo một đối tượng Map chỉ chứa các trường sẽ được lưu vào Firestore
            val jobData = hashMapOf(
                "id" to jobId,
                "companyName" to jobPostState.companyName,
                "jobTitle" to jobPostState.jobTitle,
                "description" to jobPostState.description,
                "requirement" to jobPostState.requirement,
                "address" to jobPostState.address,
                "wage" to "${jobPostState.wage} ${jobPostState.wageUnit}",
                "quantity" to (jobPostState.quantity.toIntOrNull() ?: 0),
                "workType" to jobPostState.workType.name,
                "contactName" to jobPostState.contactName,
                "contactEmail" to jobPostState.contactEmail,
                "contactPhone" to jobPostState.contactPhone,
                "postedDate" to Timestamp.now()
                // Các trường chỉ dành cho UI như iconRes và wageColor sẽ không được lưu
            )

            // Lưu đối tượng Map vào Firestore và đợi hoàn thành
            newJobDocument.set(jobData).await()
            Log.d("FirestoreRepo", "Job added successfully with ID: $jobId")

            // Trả về đối tượng Job đầy đủ để sử dụng ngay lập tức (với các giá trị UI mặc định)
            Job(
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
            )

        } catch (e: Exception) {
            Log.e("FirestoreRepo", "Error adding job to Firestore", e)
            null // Trả về null nếu có lỗi
        }
    }
}