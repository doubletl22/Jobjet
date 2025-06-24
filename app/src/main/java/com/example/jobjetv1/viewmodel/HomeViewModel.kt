package com.example.jobjetv1.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.example.jobjetv1.data.model.Job
import com.example.jobjetv1.repository.JobsRepositoryFirestore

class HomeViewModel : ViewModel() {

    // --- SỬA LỖI: Bắt đầu ---
    // 1. Tạo một thực thể (instance) của repository.
    //    Lỗi "Unresolved reference" xảy ra vì bạn đang cố truy cập `allJobs`
    //    từ tên class, nhưng nó là một thuộc tính của thực thể.
    private val jobsRepository = JobsRepositoryFirestore()
    // --- SỬA LỖI: Kết thúc ---

    // Jobs từ repository - sử dụng thuộc tính của thực thể đã tạo.
    // derivedStateOf rất hữu ích khi state của bạn được tính toán từ một hoặc nhiều state khác.
    val jobs: List<Job> by derivedStateOf { jobsRepository.allJobs }

    /**
     * Tìm kiếm jobs dựa trên query.
     * Hàm này sẽ lọc danh sách jobs hiện có trong `jobsRepository.allJobs`.
     */
    fun searchJobs(query: String): List<Job> {
        if (query.isBlank()) {
            return jobs // Trả về tất cả jobs nếu query rỗng
        }
        return jobs.filter { job ->
            job.title.contains(query, ignoreCase = true) ||
                    job.address.contains(query, ignoreCase = true) ||
                    job.description.contains(query, ignoreCase = true)
        }
    }

    /**
     * Lấy job theo ID.
     * --- SỬA LỖI: Sử dụng thực thể `jobsRepository` để gọi phương thức ---
     */
    fun getJobById(id: String): Job? {
        return jobsRepository.getJobById(id)
    }

    /**
     * Refresh jobs (không cần thiết với Firestore Listener).
     * Firestore listener trong repository sẽ tự động cập nhật `allJobs`
     * và Compose sẽ tự động nhận biết thay đổi.
     * Bạn có thể giữ hàm này trống hoặc xóa nó đi.
     */
    fun refreshJobs() {
        // Không cần implement gì ở đây vì Firestore đã xử lý real-time.
        // Repository nên tự động cập nhật `allJobs` khi có thay đổi trên server.
    }

    // Các hàm dưới đây có thể hữu ích cho việc debug, không cần thay đổi.
    /**
     * Lấy số lượng jobs hiện tại để debug.
     */
    fun getJobsCount(): Int = jobs.size

    /**
     * Lấy job mới nhất để debug.
     */
    fun getLatestJob(): Job? = jobs.firstOrNull()

    /**
     * Composable này không thực sự cần thiết vì UI sẽ tự động cập nhật
     * khi `jobs` (là một State) thay đổi. Bạn có thể xóa nó đi.
     */
    @Composable
    fun ObserveJobsChanges() {
        // LaunchedEffect sẽ được trigger mỗi khi jobs.size thay đổi.
        // Tuy nhiên, vì `jobs` đã là một state được theo dõi bởi Compose,
        // bất kỳ UI nào sử dụng nó sẽ tự động được recompose.
        LaunchedEffect(jobs.size) {
            println("Job count changed to: ${jobs.size}")
        }
    }

    /**
     * Hàm này nên được implement trong repository, không phải ViewModel.
     * ViewModel không nên có logic tạo dữ liệu trực tiếp.
     */
    fun addTestJob() {
        // Để thêm job mới, bạn nên gọi một phương thức trong repository, ví dụ:
        // jobsRepository.addJob(newJob)
        throw UnsupportedOperationException("addTestJob nên được xử lý bởi JobsRepositoryFirestore")
    }
}
