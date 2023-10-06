<script setup lang="ts">
import { onMounted, ref, reactive } from "vue";

const data = ref([]);
const total = ref(0);
const loading = ref(true);
const editId = ref(null);
const pageSize = 5;
const dialogTableVisible = ref(false);
const form = reactive({
  firstName: "",
  lastName: "",
  age: undefined,
});

const onGetTableData = async (pageNum = 1) => {
  try {
    loading.value = true;
    const result = await fetch(
      `http://localhost:8080/user/page?pageNumber=${pageNum}&pageSize=${pageSize}`
    ).then((res) => res.json());

    data.value = [...result.data.list];
    total.value = result.data.total;
  } finally {
    loading.value = false;
  }
};

const onCurrentChange = (pageNum: number) => {
  onGetTableData(pageNum);
};

onMounted(() => {
  onGetTableData();
});

const onDelete = (row) => {
  ElMessageBox.confirm("Are your sure deleted this user?", "Warning", {
    confirmButtonText: "OK",
    cancelButtonText: "Cancel",
    type: "warning",
  }).then(async () => {
    await fetch(`http://localhost:8080/user/${row.id}`, {
      method: "delete",
    });
    await onGetTableData();
    ElMessage({
      type: "success",
      message: "Delete completed",
    });
  });
};

const onEdit = (row) => {
  editId.value = row.id;
  form.firstName = row.firstName;
  form.lastName = row.lastName;
  form.age = row.age;
  dialogTableVisible.value = true;
};

const onConfirm = async () => {
  await fetch("http://localhost:8080/user", {
    method: "post",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      ...form,
    }),
  });
  dialogTableVisible.value = false;
  await onGetTableData();
  ElMessage({
    message: "Sussessful!",
    type: "success",
  });
};

const onDialogClose = () => {
  form.firstName = "";
  form.lastName = "";
  form.age = undefined;
};
</script>

<template>
  <div>
    <el-button type="primary" @click="dialogTableVisible = true"
      >Add User</el-button
    >
    <ElDialog
      v-model="dialogTableVisible"
      title="Add User"
      @close="onDialogClose"
    >
      <el-form :model="form">
        <el-form-item label="First Name">
          <el-input v-model="form.firstName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Last Name">
          <el-input v-model="form.lastName" autocomplete="off" />
        </el-form-item>
        <el-form-item label="Age">
          <el-input v-model="form.age" autocomplete="off" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogTableVisible = false">Cancel</el-button>
          <el-button type="primary" @click="onConfirm">
            Confirm
          </el-button>
        </span>
      </template>
    </ElDialog>
  </div>
  <el-table :data="data" style="width: 100%;" v-loading="loading">
    <el-table-column prop="id" label="Id" width="180" />
    <el-table-column prop="firstName" label="FirstName" width="180" />
    <el-table-column prop="lastName" label="LastName" />
    <el-table-column prop="age" label="Age" />
    <el-table-column fixed="right" label="Action" width="120">
      <template #default="scope">
        <el-button type="danger" link size="small" @click="onDelete(scope.row)"
          >Detail</el-button
        >
        <el-button link type="primary" size="small" @click="onEdit(scope.row)"
          >Edit</el-button
        >
      </template>
    </el-table-column>
  </el-table>
  <div class="pagination">
    <el-pagination
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total"
      background
      @current-change="onCurrentChange"
    />
  </div>
</template>
<style scoped>
.pagination {
  padding-top: 8px;
  display: flex;
  justify-content: flex-end;
}
</style>
